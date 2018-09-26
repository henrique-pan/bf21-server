package com.bf21.repository;

import com.bf21.entity.ClientGoal;
import com.bf21.entity.Macro;
import com.bf21.entity.Nutrient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryStarter {

    @Autowired
    private MacroDAO macroDAO;

    @Autowired
    private NutrientDAO nutrientDAO;

    @Autowired
    private ClientGoalDAO clientGoalDAO;

    @PostConstruct
    public void init() {
        loadClientGoals();
        loadMacros();
        loadNutrients();
    }

    private void loadClientGoals() {
        try {
            List<ClientGoal> list = new ArrayList<>();

            // Lose weight
            list.add(new ClientGoal(1, "Lose weight"));
            // Keep weight
            list.add(new ClientGoal(2, "Keep weight"));
            // Gain weight
            list.add(new ClientGoal(3, "Gain weight"));

            for(ClientGoal cg : list) {
                ClientGoal existentGoal = clientGoalDAO.find(cg.getIdClientGoal());
                if (existentGoal == null) {
                    clientGoalDAO.merge(cg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMacros() {
        try {
            List<Macro> list = new ArrayList<>();

            // Lose weight
            list.add(new Macro(1, "Fat"));
            // Keep weight
            list.add(new Macro(2, "Carbohydrate"));
            // Gain weight
            list.add(new Macro(3, "Protein"));

            for(Macro macro : list) {
                Macro existentMacro = macroDAO.find(macro.getIdMacro());
                if (existentMacro == null) {
                    macroDAO.merge(macro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNutrients() {
        try {
            List<Nutrient> list = new ArrayList<>();

            list.add(new Nutrient(1, "Fat", true));
            list.add(new Nutrient(2, "Carbohydrate", true));
            list.add(new Nutrient(3, "Protein", true));
            list.add(new Nutrient(4, "Fibre", false));
            list.add(new Nutrient(5, "Sugar", false));
            list.add(new Nutrient(6, "Sodium", false));
            list.add(new Nutrient(7, "Cholesterol", false));
            list.add(new Nutrient(8, "Glycemic Index", false));

            for(Nutrient nutrient : list) {
                Nutrient existentNutrient = nutrientDAO.find(nutrient.getIdNutrient());
                if (existentNutrient == null) {
                    nutrientDAO.merge(nutrient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
