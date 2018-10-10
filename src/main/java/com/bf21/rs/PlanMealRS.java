package com.bf21.rs;

import com.bf21.entity.Food;
import com.bf21.entity.FoodPlan;
import com.bf21.entity.PlanDay;
import com.bf21.entity.PlanMeal;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.entity.dto.FoodDTO;
import com.bf21.entity.dto.PlanDayDTO;
import com.bf21.entity.dto.PlanMealDTO;
import com.bf21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/foodPlan")
public class PlanMealRS {

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

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}", method = RequestMethod.GET)
    public PlanMealDTO getById(@PathVariable("idFoodPlan") Integer idFoodPlan,
                               @PathVariable("idPlanDay") Integer idPlanDay,
                               @RequestParam("idPlanMeal") Integer idPlanMeal) {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanMealDTO result;
        if (foodPlan == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planDay does not exist.";
            return result;
        }

        PlanMeal planMeal = planMealDAO.find(idPlanMeal);
        if (planMeal == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planMeal does not exist.";
            return result;
        }

        result = new PlanMealDTO(planMeal);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The playMeal has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}/planMeal/list", method = RequestMethod.GET)
    public CollectionDTO<PlanMealDTO> getAll(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                             @PathVariable("idPlanDay") Integer idPlanDay) {
        CollectionDTO<PlanMealDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);
        if (foodPlan == null) {
            resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
            resultCollection.apiMessage = "The foodPlan does not exist.";
            return resultCollection;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            resultCollection.httpStatus = HttpStatus.NOT_FOUND.value();
            resultCollection.apiMessage = "The planDay does not exist.";
            return resultCollection;
        }

        List<PlanMeal> planMeals = planMealDAO.findAllByPlanDay(planDay);
        if (!planMeals.isEmpty()) {
            planMeals.stream().forEach(planMeal -> {
                resultCollection.add(new PlanMealDTO(planMeal));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "PlanMeal list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}/planMeal/{idPlanMeal}/food/list", method = RequestMethod.POST)
    public PlanMealDTO addMealToPlan(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                     @PathVariable("idPlanDay") Integer idPlanDay,
                                     @PathVariable("idPlanMeal") Integer idPlanMeal,
                                     @RequestBody FoodDTO foodDTO) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanMealDTO result;
        if (foodPlan == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planDay does not exist.";
            return result;
        }

        PlanMeal planMeal = planMealDAO.find(idPlanMeal);
        if (planMeal == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planMeal does not exist.";
            return result;
        }

        Food food = foodDAO.find(foodDTO.getIdFood());
        if (food == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The food does not exist";
            return result;
        }

        planMeal.addFood(food);

        planMealDAO.merge(planMeal);

        result = new PlanMealDTO(planMeal);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The food was add to plan";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}", method = RequestMethod.DELETE)
    public PlanMealDTO deletePlanMeal(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                      @PathVariable("idPlanDay") Integer idPlanDay,
                                      @RequestParam("idPlanMeal") Integer idPlanMeal) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanMealDTO result;
        if (foodPlan == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planDay does not exist.";
            return result;
        }

        PlanMeal planMeal = planMealDAO.find(idPlanMeal);
        if (planMeal == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planMeal does not exist.";
            return result;
        }

        planMealDAO.remove(planMeal);

        result = new PlanMealDTO(planMeal);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The planMeal was removed";

        return result;
    }

    @RequestMapping(value = "/{idFoodPlan}/planDay/{idPlanDay}/planMeal/{idPlanMeal}/food/list", method = RequestMethod.DELETE)
    public PlanMealDTO removeFoodFromMeal(@PathVariable("idFoodPlan") Integer idFoodPlan,
                                          @PathVariable("idPlanDay") Integer idPlanDay,
                                          @PathVariable("idPlanMeal") Integer idPlanMeal,
                                          @RequestParam("idFood") Integer idFood) throws Exception {
        FoodPlan foodPlan = foodPlanDAO.find(idFoodPlan);

        PlanMealDTO result;
        if (foodPlan == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The foodPlan does not exist.";
            return result;
        }

        PlanDay planDay = planDayDAO.find(idPlanDay);
        if (planDay == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planDay does not exist.";
            return result;
        }

        PlanMeal planMeal = planMealDAO.find(idPlanMeal);
        if (planMeal == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The planMeal does not exist.";
            return result;
        }

        Food food = foodDAO.find(idFood);
        if (food == null) {
            result = new PlanMealDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The food does not exist";
            return result;
        }

        planMeal.getFoods().removeIf(f -> f.getIdFood().equals(food.getIdFood()));

        planMealDAO.merge(planMeal);

        result = new PlanMealDTO(planMeal);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The food was removed from meal";

        return result;
    }

}