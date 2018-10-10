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
    private PlanDayDAO planDayDAO;

    @Autowired
    private PlanMealDAO planMealDAO;

    @RequestMapping(method = RequestMethod.GET)
    public FoodPlanDTO getById(@RequestParam("idFoodPlan") Integer idFoodPlan) {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        FoodPlanDTO result = new FoodPlanDTO(foodPlan);
        if (foodPlan == null) {
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
        if (idClient != null && idCoach != null) {
            resultCollection.httpStatus = HttpStatus.CONFLICT.value();
            resultCollection.apiMessage = "Only idClient or idCoach should be passed as a parameter";
            return resultCollection;
        } else if (idClient != null) {
            Client client = clientDAO.find(idClient);
            if (client == null) {
                resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
                resultCollection.apiMessage = "The client does not exist.";
                return resultCollection;
            }

            foodPlans = foodPlanDAO.findAllByClient(client);
        } else if (idCoach != null) {
            Coach coach = coachDAO.find(idCoach);
            if (coach == null) {
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
        if (client == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
        } else {
            Client existentClient = clientDAO.find(client.getIdClient());
            if (existentClient == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The client id does not exist";
                return result;
            } else {
                foodPlan.setClient(existentClient);
            }
        }

        CoachDTO coach = foodPlanDTO.getCoach();
        if (coach == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
        } else {
            Coach existentCoach = coachDAO.find(coach.getIdCoach());
            if (existentCoach == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The coach id does not exist";
                return result;
            } else {
                foodPlan.setCoach(existentCoach);
            }
        }

        List<PlanDayDTO> planDays = foodPlanDTO.getPlanDays();
        if (planDays != null && !planDays.isEmpty()) {
            for (PlanDayDTO pd : planDays) {
                PlanDay planDay = new PlanDay();
                planDay.setFoodPlan(foodPlan);
                planDay.setDay(pd.getDay());
                planDay.setCreationDate(new Date());

                List<PlanMealDTO> planMeals = pd.getPlanMeals();
                if (planMeals != null && !planMeals.isEmpty()) {
                    for (PlanMealDTO pm : planMeals) {
                        PlanMeal planMeal = new PlanMeal();
                        planMeal.setPlanDay(planDay);
                        planMeal.setName(pm.getName());
                        planMeal.setOrder(pm.getOrder());
                        planMeal.setCreationDate(new Date());

                        List<FoodDTO> foods = pm.getFoods();
                        if (foods != null && !foods.isEmpty()) {
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
    public FoodPlanDTO updateFoodPlan(@RequestBody FoodPlanDTO foodPlan) throws Exception {
        FoodPlanDTO result;

        Integer idFoodPlan = foodPlan.getIdFoodPlan();
        if (idFoodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idFoodPlan is mandatory";
            return result;
        }

        FoodPlan existentFoodPlan = foodPlanDAO.find(foodPlan.getIdFoodPlan());
        if (existentFoodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The foodPlan does not exist";
            return result;
        }
        existentFoodPlan.setPlanName(foodPlan.getPlanName());
        existentFoodPlan.setModificationDate(new Date());

        ClientDTO client = foodPlan.getClient();
        if (client == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
        } else {
            Integer idClient = client.getIdClient();
            if (idClient == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The idClient is mandatory";
                return result;
            }

            Client existentClient = clientDAO.find(idClient);
            if (existentClient == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The client does not exist";
                return result;
            }

            existentFoodPlan.setClient(existentClient);
        }

        CoachDTO coach = foodPlan.getCoach();
        if (coach == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
        } else {
            Integer idCoach = coach.getIdCoach();
            if (idCoach == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The idCoach is mandatory";
                return result;
            }

            Coach existentCoach = coachDAO.find(idCoach);
            if (existentCoach == null) {
                result = new FoodPlanDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The coach does not exist";
                return result;
            }

            existentFoodPlan.setCoach(existentCoach);
        }

        List<PlanDayDTO> planDays = foodPlan.getPlanDays();
        if (planDays != null && !planDays.isEmpty()) {
            List<PlanDay> planDayList = new ArrayList<>();
            for (PlanDayDTO pd : planDays) {
                Integer idPlanDay = pd.getIdPlanDay();
                if (idPlanDay == null) {
                    result = new FoodPlanDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "The idPlanDay is mandatory";
                    return result;
                }

                PlanDay existentPlanDay = planDayDAO.find(idPlanDay);
                if (existentPlanDay == null) {
                    result = new FoodPlanDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "The planDay does not exist";
                    return result;
                }

                existentPlanDay.setModificationDate(new Date());
                planDayList.add(existentPlanDay);

                List<PlanMealDTO> planMeals = pd.getPlanMeals();
                if (planMeals != null && !planMeals.isEmpty()) {
                    List<PlanMeal> planMealList = new ArrayList<>();
                    for (PlanMealDTO pm : planMeals) {
                        Integer idPlanMeal = pm.getIdPlanMeal();
                        if (idPlanMeal == null) {
                            result = new FoodPlanDTO();
                            result.httpStatus = HttpStatus.BAD_REQUEST.value();
                            result.apiMessage = "The idPlanMeal is mandatory";
                            return result;
                        }

                        PlanMeal existentPlanMeal = planMealDAO.find(idPlanDay);
                        if (existentPlanMeal == null) {
                            result = new FoodPlanDTO();
                            result.httpStatus = HttpStatus.BAD_REQUEST.value();
                            result.apiMessage = "The planMeal does not exist";
                            return result;
                        }

                        existentPlanMeal.setModificationDate(new Date());
                        planMealList.add(existentPlanMeal);

                        List<FoodDTO> foods = pm.getFoods();
                        if (foods != null && !foods.isEmpty()) {
                            List<Food> foodList = new ArrayList<>();
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

                                foodList.add(food);
                            }
                            existentPlanMeal.setFoods(foodList);
                        }
                    }
                    existentPlanDay.setPlanMeals(planMealList);
                }
                existentFoodPlan.setPlanDays(planDayList);
            }
        }

        foodPlanDAO.merge(existentFoodPlan);

        result = new FoodPlanDTO(existentFoodPlan);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The foodPlan was updated successfully";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/list", method = RequestMethod.POST)
    public FoodPlanDTO addDayToPlan(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                    @RequestBody PlanDayDTO planDayDTO) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        FoodPlanDTO result;
        if (foodPlan == null) {
            result = new FoodPlanDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = new PlanDay();
        planDay.setFoodPlan(foodPlan);
        planDay.setDay(planDayDTO.getDay());
        planDay.setCreationDate(new Date());

        List<PlanMealDTO> planMeals = planDayDTO.getPlanMeals();
        if (planMeals != null && !planMeals.isEmpty()) {
            for (PlanMealDTO pm : planMeals) {
                PlanMeal planMeal = new PlanMeal();
                planMeal.setPlanDay(planDay);
                planMeal.setName(pm.getName());
                planMeal.setOrder(pm.getOrder());
                planMeal.setCreationDate(new Date());

                List<FoodDTO> foods = pm.getFoods();
                if (foods != null && !foods.isEmpty()) {
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

        foodPlanDAO.merge(foodPlan);

        result = new FoodPlanDTO(foodPlan);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The planDay was add to plan";

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public FoodPlanDTO deleteFoodPlan(@RequestParam("idFoodPlan") Integer idFoodPlan) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        FoodPlanDTO result;

        if (foodPlan == null) {
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

    @RequestMapping(value = "/{idFoodPlan}/planDay/list", method = RequestMethod.DELETE)
    public PlanDayDTO removeDayFromPlan(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                        @RequestParam("idPlanDay") Integer idPlanDay) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanDayDTO result;
        if (foodPlan == null) {
            result = new PlanDayDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanDayDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planDay does not exist.";
            return result;
        }

        foodPlan.getPlanDays().removeIf(pd -> pd.getIdPlanDay().equals(planDay.getIdPlanDay()));

        foodPlanDAO.merge(foodPlan);

        result = new PlanDayDTO(planDay);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The planDay was removed from meal";

        return result;
    }

}
