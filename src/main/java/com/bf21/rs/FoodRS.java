package com.bf21.rs;

import com.bf21.entity.Food;
import com.bf21.entity.FoodNutrient;
import com.bf21.entity.Macro;
import com.bf21.entity.Nutrient;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.entity.dto.FoodDTO;
import com.bf21.entity.dto.NutrientDTO;
import com.bf21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/food")
public class FoodRS {

    @Autowired
    private FoodDAO foodDAO;

    @Autowired
    private MacroDAO macroDAO;

    @Autowired
    private NutrientDAO nutrientDAO;

    @RequestMapping(method = RequestMethod.GET)
    public FoodDTO getById(@RequestParam("idFood") Integer idFood) {
        Food food = foodDAO.find(idFood);

        FoodDTO result = new FoodDTO(food);
        if (food == null) {
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The food does not exist.";
            return result;
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The food has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CollectionDTO<FoodDTO> getAll(@RequestParam(name = "query", required = false) String query) {
        CollectionDTO<FoodDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Food> foods;
        if (query != null) {
            foods = foodDAO.findAllByQuery(query);
        } else {
            foods = foodDAO.findAll();
        }

        if (!foods.isEmpty()) {
            foods.forEach(client -> {
                resultCollection.add(new FoodDTO(client));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Food list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(method = RequestMethod.POST)
    public FoodDTO createFood(@RequestBody FoodDTO food) throws Exception {
        FoodDTO result;

        if (food.getIdFood() != null) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idFood should not exist";
            return result;
        }

        List<Food> foods = foodDAO.findByName(food.getName());
        if (food.getBrand() == null && foods != null && !foods.isEmpty()) {
            for (Food f : foods) {
                if (f.getBrand() == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "Duplicate name: " + food.getName();
                    return result;
                }
            }
        }

        Food newFood = new Food();
        newFood.setName(food.getName());
        newFood.setBrand(food.getBrand());
        newFood.setPortionSize(food.getPortionSize());

        Set<NutrientDTO> nutrients = food.getNutrients();
        if (nutrients == null || nutrients.size() != 8) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The nutrient list must contain all the nutrients";
            return result;
        } else {
            for (NutrientDTO fn : nutrients) {
                Nutrient n = nutrientDAO.find(fn.getIdNutrient());
                if (n == null || fn.getTotal() == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "idNutrient invalid: " + fn.getIdNutrient();
                    return result;
                } else {
                    newFood.addNutrient(n, fn.getTotal());
                }
            }
        }

        Set<Macro> macros = food.getMacros();
        if (macros != null && !macros.isEmpty()) {
            for (Macro macro : macros) {
                Macro m = macroDAO.find(macro.getIdMacro());
                if (m == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "idMacro invalid: " + macro.getIdMacro();
                    return result;
                } else {
                    newFood.addMacro(m);
                }
            }
        }

        foodDAO.persist(newFood);

        result = new FoodDTO(newFood);
        result.httpStatus = HttpStatus.CREATED.value();
        result.apiMessage = "The food was created successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public FoodDTO updateFood(@RequestBody FoodDTO food) throws Exception {
        FoodDTO result;

        if (food.getIdFood() == null) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idFood is mandatory";
            return result;
        }

        Food existentFood = foodDAO.find(food.getIdFood());
        if (existentFood == null) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The food does not exist";
            return result;
        }

        List<Food> foods = foodDAO.findByName(food.getName());
        if (food.getBrand() == null && foods != null && !foods.isEmpty()) {
            for (Food f : foods) {
                if (f.getBrand() == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "Duplicate name: " + food.getName();
                    return result;
                }
            }
        }

        Food updatedFood = new Food();
        updatedFood.setIdFood(food.getIdFood());
        updatedFood.setName(food.getName());
        updatedFood.setBrand(food.getBrand());
        updatedFood.setPortionSize(food.getPortionSize());
        updatedFood.setCreationDate(existentFood.getCreationDate());

        Set<NutrientDTO> nutrients = food.getNutrients();
        if (nutrients == null || nutrients.size() != 8) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The nutrient list must contain all the nutrients";
            return result;
        } else {
            for (NutrientDTO fn : nutrients) {
                Nutrient n = nutrientDAO.find(fn.getIdNutrient());
                if (n == null || fn.getTotal() == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "idNutrient invalid: " + fn.getIdNutrient();
                    return result;
                } else {
                    updatedFood.addNutrient(n, fn.getTotal());
                }
            }
        }

        Set<Macro> macros = food.getMacros();
        if (macros != null && !macros.isEmpty()) {
            for (Macro macro : macros) {
                Macro m = macroDAO.find(macro.getIdMacro());
                if (m == null) {
                    result = new FoodDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "idMacro invalid: " + macro.getIdMacro();
                    return result;
                } else {
                    updatedFood.addMacro(m);
                }
            }
        }

        foodDAO.merge(updatedFood);

        result = new FoodDTO(updatedFood);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The food was update successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public FoodDTO deleteFood(@RequestParam("idFood") Integer idFood) throws Exception {
        Food food = foodDAO.find(idFood);

        FoodDTO result;

        if (food == null) {
            result = new FoodDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The food does not exist";
            return result;
        }

        foodDAO.remove(food);

        result = new FoodDTO(food);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The food was removed";

        return result;
    }

}
