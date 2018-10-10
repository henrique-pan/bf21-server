package com.bf21.rs;

import com.bf21.entity.*;
import com.bf21.entity.dto.*;
import com.bf21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/foodPlan")
public class PlanDayRS {

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

    @RequestMapping(value = "/{idFoodPlan}", method = RequestMethod.GET)
    public PlanDayDTO getById(@PathVariable("idFoodPlan") Integer idFoodPlan,
                              @RequestParam("idPlanDay") Integer idPlanDay) {
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

        result = new PlanDayDTO(planDay);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The playDay has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/list", method = RequestMethod.GET)
    public CollectionDTO<PlanDayDTO> getAll(@PathVariable("idFoodPlan") Integer idFoodPlan) {
        CollectionDTO<PlanDayDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);
        if (foodPlan == null) {
            resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
            resultCollection.apiMessage = "The foodPlan does not exist.";
            return resultCollection;
        }

        List<PlanDay> planDays = planDayDAO.findAllByPlan(foodPlan);
        if (!planDays.isEmpty()) {
            planDays.stream().forEach(planDay -> {
                resultCollection.add(new PlanDayDTO(planDay));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "FoodPlan list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}/planMeal/list", method = RequestMethod.POST)
    public PlanDayDTO addMealToPlan(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                    @PathVariable("idPlanDay") Integer idPlanDay,
                                    @RequestBody PlanMealDTO planMealDTO) throws Exception {
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

        PlanMeal planMeal = new PlanMeal();
        planMeal.setPlanDay(planDay);
        planMeal.setName(planMealDTO.getName());
        planMeal.setOrder(planMealDTO.getOrder());
        planMeal.setCreationDate(new Date());

        List<FoodDTO> foods = planMealDTO.getFoods();
        if (foods != null && !foods.isEmpty()) {
            for (FoodDTO f : foods) {
                if (f.getIdFood() == null) {
                    result = new PlanDayDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "The idFood is mandatory";
                    return result;
                }

                Food food = foodDAO.find(f.getIdFood());
                if (food == null) {
                    result = new PlanDayDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "The food does not exist";
                    return result;
                }
                planMeal.addFood(food);
            }
        }

        planDay.addPlanMeal(planMeal);

        planDayDAO.merge(planDay);

        result = new PlanDayDTO(planDay);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The planMeal was add to plan";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay", method = RequestMethod.DELETE)
    public PlanDayDTO deletePlanDay(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                    @RequestParam("idPlanDay") Integer idPlanDay) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanDayDTO result;

        if (foodPlan == null) {
            result = new PlanDayDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The foodPlan does not exist";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanDayDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The planDay does not exist";
            return result;
        }

        planDayDAO.remove(planDay);

        result = new PlanDayDTO(planDay);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The planDay was removed";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}/planMeal/list", method = RequestMethod.DELETE)
    public PlanDayDTO removeMealFromDay(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                        @PathVariable("idPlanDay") Integer idPlanDay,
                                        @RequestParam("idPlanMeal") Integer idPlanMeal) throws Exception {
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

        PlanMeal planMeal = planMealDAO.find(idPlanMeal);
        if (planMeal == null) {
            result = new PlanDayDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planMeal does not exist.";
            return result;
        }

        planDay.getPlanMeals().removeIf(pm -> pm.getIdPlanMeal().equals(planMeal.getIdPlanMeal()));

        planDayDAO.merge(planDay);

        result = new PlanDayDTO(planDay);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The meal was removed from meal";

        return result;
    }

}
