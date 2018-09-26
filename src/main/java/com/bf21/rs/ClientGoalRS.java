package com.bf21.rs;

import com.bf21.entity.Client;
import com.bf21.entity.ClientGoal;
import com.bf21.entity.dto.ClientDTO;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.ClientDAO;
import com.bf21.repository.ClientGoalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client/goal")
public class ClientGoalRS {

    @Autowired
    private ClientGoalDAO clientGoalDAO;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CollectionDTO<ClientGoal> getGoals() {
        CollectionDTO<ClientGoal> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<ClientGoal> goals = clientGoalDAO.findAll();

        if (!goals.isEmpty()) {
            goals.forEach(resultCollection::add);

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "ClientGoal list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

}
