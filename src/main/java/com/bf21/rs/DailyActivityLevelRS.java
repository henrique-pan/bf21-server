package com.bf21.rs;

import com.bf21.entity.DailyActivityLevel;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.DailyActivityLevelDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client/dailyActivityLevel")
public class DailyActivityLevelRS {

    @Autowired
    private DailyActivityLevelDAO dailyActivityLevelDAO;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CollectionDTO<DailyActivityLevel> getDailyActivityLevels() {
        CollectionDTO<DailyActivityLevel> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<DailyActivityLevel> dailyActivityLevels = dailyActivityLevelDAO.findAll();

        if (!dailyActivityLevels.isEmpty()) {
            dailyActivityLevels.forEach(resultCollection::add);

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "DailyActivityLevel list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

}
