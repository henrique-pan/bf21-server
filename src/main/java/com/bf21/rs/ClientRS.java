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
@RequestMapping("/client")
public class ClientRS {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private ClientGoalDAO clientGoalDAO;

    @RequestMapping(method = RequestMethod.GET)
    public ClientDTO getById(@RequestParam("idClient") Integer idClient) {
        Client client = clientDAO.find(idClient);

        ClientDTO result = new ClientDTO(client);
        if(client == null) {
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The client does not exist.";
            return result;
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The client has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CollectionDTO<ClientDTO> getAll(@RequestParam(name = "query", required = false) String query) {
        CollectionDTO<ClientDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Client> clients;
        if(query != null) {
            clients = clientDAO.findAllByQuery(query);
        } else {
            clients = clientDAO.findAll();
        }

        if (!clients.isEmpty()) {
            clients.stream().forEach(client -> {
                resultCollection.add(new ClientDTO(client));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Client list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ClientDTO createClient(@RequestBody Client client) throws Exception {
        ClientDTO result;

        ClientGoal clientGoal = client.getClientGoal();
        if(clientGoal == null) {
            result = new ClientDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The the client goal does not exist";
        } else {
            clientGoal = clientGoalDAO.find(clientGoal.getIdClientGoal());
            if(clientGoal == null) {
                result = new ClientDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The the client goal id does not exist";
                return result;
            }
        }

        clientDAO.persist(client);

        result = new ClientDTO(client);
        result.httpStatus = HttpStatus.CREATED.value();
        result.apiMessage = "The client was created successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ClientDTO updateClient(@RequestBody Client client) throws Exception {
        ClientDTO result;

        Integer idClient = client.getIdClient();
        if(idClient == null) {
            result = new ClientDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idClient is mandatory";
            return result;
        }

        Client existentClient = clientDAO.find(client.getIdClient());
        if(existentClient == null) {
            result = new ClientDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
            return result;
        }

        ClientGoal clientGoal = client.getClientGoal();
        if(clientGoal == null) {
            result = new ClientDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The the client goal does not exist";
        } else {
            clientGoal = clientGoalDAO.find(clientGoal.getIdClientGoal());
            if(clientGoal == null) {
                result = new ClientDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The the client goal id does not exist";
                return result;
            }
        }

        client.setCreationDate(existentClient.getCreationDate());
        clientDAO.merge(client);

        result = new ClientDTO(client);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The client was updated successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ClientDTO deleteClient(@RequestParam("idClient") Integer idClient) throws Exception {
        Client client = clientDAO.find(idClient);

        ClientDTO result;

        if(client == null) {
            result = new ClientDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The client does not exist";
            return result;
        }

        clientDAO.remove(client);

        result = new ClientDTO(client);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The client was removed";

        return result;
    }

}
