package com.bf21.rs;

import com.bf21.entity.Macro;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.MacroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/food")
public class MacroRS {

    @Autowired
    private MacroDAO macroDAO;

    @RequestMapping(value = "/macro/list",method = RequestMethod.GET)
    public CollectionDTO<Macro> getMacros() {
        CollectionDTO<Macro> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Macro> macros = macroDAO.findAll();

        if (!macros.isEmpty()) {
            macros.forEach(resultCollection::add);

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Macro list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

}
