package com.bf21.repository;

import com.bf21.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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

    @Autowired
    private DailyActivityLevelDAO dailyActivityLevelDAO;

    @Autowired
    private ProteinRequirementDAO proteinRequirementDAO;

    @PostConstruct
    public void init() {
        loadClientGoals();
        loadMacros();
        loadNutrients();
        loadDailyActivityLevels();
        loadProteinRequirements();
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

    private void loadDailyActivityLevels() {
        try {
            List<DailyActivityLevel> list = new ArrayList<>();

            list.add(new DailyActivityLevel(1, "Very Light", new BigDecimal(1.30)));
            list.add(new DailyActivityLevel(2, "Light", new BigDecimal(1.55)));
            list.add(new DailyActivityLevel(3, "Moderate", new BigDecimal(1.65)));
            list.add(new DailyActivityLevel(4, "Heavy", new BigDecimal(1.80)));

            for(DailyActivityLevel dailyActivityLevel : list) {
                DailyActivityLevel existentDailyActivityLevel = dailyActivityLevelDAO.find(dailyActivityLevel.getIdDailyActivityLevel());
                if (existentDailyActivityLevel == null) {
                    dailyActivityLevelDAO.merge(dailyActivityLevel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProteinRequirements() {
        try {
            List<ProteinRequirement> list = new ArrayList<>();

            list.add(new ProteinRequirement(1, "Sedentary Adult (RDA)", new BigDecimal(0.40)));
            list.add(new ProteinRequirement(2, "Adult Recreational Exerciser", new BigDecimal(0.75)));
            list.add(new ProteinRequirement(3, "Adult Competitive Athlete", new BigDecimal(0.90)));
            list.add(new ProteinRequirement(4, "Adult Building Muscle Mass", new BigDecimal(0.90)));
            list.add(new ProteinRequirement(5, "Dieting Athlete", new BigDecimal(1.00)));
            list.add(new ProteinRequirement(6, "Growing Teenage Athlete", new BigDecimal(1.00)));

            for(ProteinRequirement proteinRequirement : list) {
                ProteinRequirement existentProteinRequirement = proteinRequirementDAO.find(proteinRequirement.getIdProteinRequirement());
                if (existentProteinRequirement == null) {
                    proteinRequirementDAO.merge(proteinRequirement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
