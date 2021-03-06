package ru.cs.eatright.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import ru.cs.eatright.knowledgebase.Bootstrapper;
import ru.cs.eatright.knowledgebase.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ResponseCreator {

    private static final Logger logger = LoggerFactory.getLogger(ResponseCreator.class);
    private static final String emptyResponse = "Кажется ваш вопрос не относится к еде. Попробуйте еще раз?\n";
    private static final String generalInfo = "Продукты, упомянутые в вашем запросе имеют следующие характеристики (на 100 грамм): '%s'.\n";
    private static final String normalAverageCaloricity = "Общая калорийность продуктов в вашем запросе '%.1f'cal.\n";
    //private static final String hugeAverageCaloricity = "Калорийность продуктов в вашем запросе '%.1f'cal. Вы точно собираетесь это есть?!\n";
    private static final String goodСhoice = "Отличный выбор";
    private static final String goodСombination = "Хороший выбор";
    private static final String averageСombination = "Среднее сочетание продуктов";
    private static final String badCombination = "Плохое сочетание продуктов";
    private static final String badСhoice = "Вы точно хотите такое сочетание съесть?";


    //todo map
    //public static String createResponse(List<ParsedQuery> parsedQueries, MAP<???>) {

    public static String createResponse(List<ParsedQuery> parsedQueries) {

        if (parsedQueries.isEmpty()) {
            logger.info("INFO_parsedQueries.isEmpty() == true");
            return emptyResponse;
        }

        String productsInfo = "";
        StringBuilder productsInfo2 = new StringBuilder();

        int productNum = 0;
        double commonCaloricity = 0;
        for (ParsedQuery parsedQuery : parsedQueries) {
            for (Product product : parsedQuery.getProducts()) {
                productsInfo += product.toString();
                productNum++;

                // todo: add *gramm
                commonCaloricity += product.getCalorie();
            }
        }
        Product[] array_product;
        array_product = new Product[productNum];
        int pos = 0;
        for (ParsedQuery parsedQuery : parsedQueries) {
            for (Product product : parsedQuery.getProducts()) {
                array_product[pos] = product;
                pos++;
            }
        }


        try {

            File x = new File("");
            String pathfortable = x.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator +
                    "resources" + File.separator + "table" + File.separator + "table.txt";
            Scanner input = new Scanner(new File(pathfortable));

            int[][] table;
            int n = input.nextInt();

            int[][] ArrayType;
            ArrayType = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    ArrayType[i][j] = input.nextInt();
                }
            }

            for (int i = 0; i < productNum; i++) {
                for (int j = i + 1; j < productNum; j++) {
                    logger.info("productNum = " + productNum);
                    int typeproduct1 = array_product[i].getType();
                    int typeproduct2 = array_product[j].getType();
                    logger.info(typeproduct1 + " " + typeproduct2 + "&");
                    if (typeproduct1 != 0 && typeproduct2 != 0) {
                        int t = ArrayType[typeproduct1][typeproduct2];
                        logger.info("t =  " + t + "&");
                        if (t == 1) {
                            productsInfo2.append(badСhoice + "(" + array_product[i].getName() + ";" + array_product[j].getName() + ")\n");
                        }
                        if (t == 2) {
                            productsInfo2.append(badCombination + "(" + array_product[i].getName() + ";" + array_product[j].getName() + ")\n");

                        }
                        if (t == 3) {
                            productsInfo2.append(averageСombination + "(" + array_product[i].getName() + ";" + array_product[j].getName() + ")\n");

                        }
                        if (t == 4) {
                            productsInfo2.append(goodСombination + "(" + array_product[i].getName() + ";" + array_product[j].getName() + ")\n");

                        }
                        if (t == 5) {
                            productsInfo2.append(goodСhoice + "(" + array_product[i].getName() + ";" + array_product[j].getName() + ")\n");

                        }
                    }
                }
            }
            String response = String.format(generalInfo, productsInfo);
            response += productsInfo2.toString();
            response +=  String.format(normalAverageCaloricity, commonCaloricity);
            logger.info(response);
            return response;
        } catch (Exception e) {

            return emptyResponse;
        }
    }
}