package com.bf21.rs;

import com.bf21.entity.ProteinRequirement;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.ProteinRequirementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client/proteinRequirement")
public class ProteinRequirementRS {

    @Autowired
    private ProteinRequirementDAO proteinRequirementDAO;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CollectionDTO<ProteinRequirement> getProteinRequirements() {
        CollectionDTO<ProteinRequirement> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<ProteinRequirement> proteinRequirements = proteinRequirementDAO.findAll();

        if (!proteinRequirements.isEmpty()) {
            proteinRequirements.forEach(resultCollection::add);

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "ProteinRequirement list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

}
