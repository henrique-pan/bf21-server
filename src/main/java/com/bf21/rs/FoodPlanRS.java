package com.bf21.rs;

import com.bf21.entity.*;
import com.bf21.entity.dto.*;
import com.bf21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/foodPlan")
public class FoodPlanRS {

    @Autowired
    private FoodDAO foodDAO;

    @Autowired
    private CoachDAO coachDAO;

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private FoodPlanDAO foodPlanDAO;

    @Autowired
    private PlanMealDAO planMealDAO;

    @RequestMapping(method = RequestMethod.GET)
    public FoodPlanDTO getById(@RequestParam("idFoodPlan") Integer idFoodPlan) {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        FoodPlanDTO result = new FoodPlanDTO(foodPlan);
        if(foodPlan == null) {
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The foodPlan has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CollectionDTO<FoodPlanDTO> getAll(@RequestParam(name = "idClient", required = false) Integer idClient,
                                             @RequestParam(name = "idCoach", required = false) Integer idCoach) {
        CollectionDTO<FoodPlanDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<FoodPlan> foodPlans;
        if(idClient != null && idCoach != null) {
            resultCollection.httpStatus = HttpStatus.CONFLICT.value();
            resultCollection.apiMessage = "Only idClient or idCoach should be passed as a parameter";
            return resultCollection;
        }else if(idClient != null) {
            Client client = clientDAO.find(idClient);
            if(client == null) {
                resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
                resultCollection.apiMessage = "The client does not exist.";
                return resultCollection;
            }

            foodPlans = foodPlanDAO.findAllByClient(client);
        } else if(idCoach != null) {
            Coach coach = coachDAO.find(idCoach);
            if(coach == null) {
                resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
                resultCollection.apiMessage = "The coach does not exist.";
                return resultCollection;
            }

            foodPlans = foodPlanDAO.findAllByCoach(coach);
        } else {
            foodPlans = foodPlanDAO.findAll();
        }

        if (!foodPlans.isEmpty()) {
            foodPlans.stream().forEach(foodPlan -> {
                resultCollection.add(new FoodPlanDTO(foodPlan));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "FoodPlan list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(method = RequestMethod.POST)
    public FoodPlanDTO createFoodPlan(@RequestBody FoodPlanDTO foodPlanDTO) throws Exception {
        FoodPlanDTO result;

        FoodPlan foodPlan = new FoodPlan();
        foodPlan.setPlanName(foodPlanDTO.getPlanName());

        ClientDTO client = foodPlanDTO.getClient();
        if(client == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
        } else {
            Client existentClient = clientDAO.find(client.getIdClient());
            if(existentClient == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The client id does not exist";
                return result;
            } else {
                foodPlan.setClient(existentClient);
            }
        }

        CoachDTO coach = foodPlanDTO.getCoach();
        if(coach == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
        } else {
            Coach existentCoach = coachDAO.find(coach.getIdCoach());
            if(existentCoach == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The coach id does not exist";
                return result;
            } else {
                foodPlan.setCoach(existentCoach);
            }
        }

        List<PlanDayDTO> planDays = foodPlanDTO.getPlanDays();
        if(planDays != null & !planDays.isEmpty()) {
            for(PlanDayDTO pd : planDays) {
                PlanDay planDay = new PlanDay();
                planDay.setFoodPlan(foodPlan);
                planDay.setDay(pd.getDay());
                planDay.setCreationDate(new Date());

                List<PlanMealDTO> planMeals = pd.getPlanMeals();
                if(planMeals != null && !planMeals.isEmpty()) {
                    for(PlanMealDTO pm : planMeals) {
                        PlanMeal planMeal = new PlanMeal();
                        planMeal.setPlanDay(planDay);
                        planMeal.setName(pm.getName());
                        planMeal.setOrder(pm.getOrder());
                        planMeal.setCreationDate(new Date());

                        List<FoodDTO> foods = pm.getFoods();
                        if(foods != null && !foods.isEmpty()) {
                            for (FoodDTO f : foods) {
                                if (f.getIdFood() == null) {
                                    result = new FoodPlanDTO();
                                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                                    result.apiMessage = "The idFood is mandatory";
                                    return result;
                                }

                                Food food = foodDAO.find(f.getIdFood());
                                if (food == null) {
                                    result = new FoodPlanDTO();
                                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                                    result.apiMessage = "The food does not exist";
                                    return result;
                                }
                                planMeal.addFood(food);
                            }
                        }
                        planDay.addPlanMeal(planMeal);
                    }
                }
                foodPlan.addPlanDay(planDay);
            }
        }

        foodPlanDAO.persist(foodPlan);

        result = new FoodPlanDTO(foodPlan);
        result.httpStatus = HttpStatus.CREATED.value();
        result.apiMessage = "The foodPlan was created successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public FoodPlanDTO updateFoodPlan(@RequestBody FoodPlan foodPlan) throws Exception {
        FoodPlanDTO result;

        Integer idFoodPlan = foodPlan.getIdFoodPlan();
        if(idFoodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idFoodPlan is mandatory";
            return result;
        }

        FoodPlan existentFoodPlan = foodPlanDAO.find(foodPlan.getIdFoodPlan());
        if(existentFoodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The foodPlan does not exist";
            return result;
        }

        Client client = foodPlan.getClient();
        if(client == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
        } else {
            client = clientDAO.find(client.getIdClient());
            if(client == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The client id does not exist";
                return result;
            } else {
                foodPlan.setClient(client);
            }
        }

        Coach coach = foodPlan.getCoach();
        if(coach == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
        } else {
            coach = coachDAO.find(coach.getIdCoach());
            if(coach == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The coach id does not exist";
                return result;
            } else {
                foodPlan.setCoach(coach);
            }
        }

        foodPlan.setCreationDate(existentFoodPlan.getCreationDate());
        foodPlanDAO.merge(foodPlan);

        result = new FoodPlanDTO(foodPlan);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The foodPlan was updated successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public FoodPlanDTO deleteFoodPlan(@RequestParam("idFoodPlan") Integer idFoodPlan) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        FoodPlanDTO result;

        if(foodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The foodPlan does not exist";
            return result;
        }

        foodPlanDAO.remove(foodPlan);

        result = new FoodPlanDTO(foodPlan);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The foodPlan was removed";

        return result;
    }

}
