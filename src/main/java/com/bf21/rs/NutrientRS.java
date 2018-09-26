package com.bf21.rs;

import com.bf21.entity.Nutrient;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.NutrientDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/food")
public class NutrientRS {

    @Autowired
    private NutrientDAO nutrientDAO;

    @RequestMapping(value = "/nutrient/list", method = RequestMethod.GET)
    public CollectionDTO<Nutrient> getNutrients() {
        CollectionDTO<Nutrient> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Nutrient> nutrients = nutrientDAO.findAll();

        if (!nutrients.isEmpty()) {
            nutrients.forEach(resultCollection::add);

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Nutrient list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

}
