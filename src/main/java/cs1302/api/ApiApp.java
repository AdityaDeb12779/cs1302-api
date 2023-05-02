package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static final String DEFAULT_IMG = "file:resources/default.jpeg";
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    Stage stage;
    Scene scene;
    VBox root;

    StackPane topArea;
    ImageView backgroundImage;
    HBox searchArea;
    TextField urlSearch;
    Button searchButton;

    ScrollPane scroll;

    VBox recipeInfo;
    HBox picAndLabels;
    Label yield;
    VBox nameAndLabels;
    ImageView foodPic;
    Label labels;
    Text recipeName;
    HBox textArea;
    Label ingredients;
    Text instructions;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        topArea = new StackPane();
        backgroundImage = new ImageView(DEFAULT_IMG);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitWidth(600);
        backgroundImage.setFitHeight(450);
        backgroundImage.setSmooth(true);

        searchArea = new HBox(8);
        searchArea.setAlignment(Pos.CENTER);
        urlSearch = new TextField("Alfredo Pasta");
        searchArea.setHgrow(urlSearch, Priority.ALWAYS);
        searchButton = new Button("Search");

        recipeName = new Text("Search for a Recipe in the Search Box");
        recipeName.setWrappingWidth(backgroundImage.getFitWidth());
        recipeName.setTextAlignment(TextAlignment.JUSTIFY);


        recipeInfo = new VBox(4);
        recipeInfo.setAlignment(Pos.CENTER);
        picAndLabels = new HBox(8);
        picAndLabels.setAlignment(Pos.CENTER);
        nameAndLabels = new VBox(4);
        picAndLabels.setAlignment(Pos.CENTER);
        yield = new Label();
        foodPic = new ImageView();
        foodPic.setPreserveRatio(true);
        labels = new Label();
        labels.setWrapText(true);
        labels.setTextAlignment(TextAlignment.JUSTIFY);
        labels.setMaxWidth(350);
        textArea = new HBox();
        ingredients = new Label();
        instructions = new Text();

        scroll = new ScrollPane(recipeInfo);

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("init() called");

        searchArea.getChildren().addAll(urlSearch, searchButton);
        searchArea.setMargin(urlSearch, new Insets(0, 0, 0, 10));
        searchArea.setMargin(searchButton, new Insets(0, 10, 0, 0));

        topArea.getChildren().addAll(backgroundImage, searchArea);

        textArea.getChildren().addAll(ingredients, instructions);

        nameAndLabels.getChildren().addAll(recipeName, yield, labels);
        picAndLabels.getChildren().addAll(foodPic, nameAndLabels);
        recipeInfo.getChildren().addAll(picAndLabels, textArea);

        root.getChildren().addAll(topArea, scroll);

        searchButton.setOnAction(event -> getRecipe());
    } // init


    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // setup scene
        scene = new Scene(root);
        scene.setFill(Color.BEIGE);

         // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.show();

    } // start

    private void getRecipe() {
        String search = urlSearch.getText().replaceAll(" ", "%20");
        search = search.toLowerCase();
        String app_id = "47c19ceb";
        String app_key = "70f21e3ad8feded302fe0126077a68b3";
        String query = String.format("&q=%s&app_id=%s&app_key=%s&ingr=10", search, app_id, app_key);
        String uri = "https://api.edamam.com/api/recipes/v2?type=public" + query;
        System.out.println(uri);

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if

            String jsonString = response.body();
            EdamamResponse edamamResponse = GSON.fromJson(jsonString, EdamamResponse.class);

            String app_id2 = "fd78c514";
            String app_key2 = "c04320424b88b5cad6f5993f4008ce49";
            String uri2 = "https://api.edamam.com/api/nutrition-data?";
            for (int i = 0; i < 1; i++) {
                System.out.println("Ingredient: " + edamamResponse.hits[0].recipe.ingredientLines[i]);
                String search2 = edamamResponse.hits[0].recipe.ingredientLines[i];
                search2 = search2.replaceAll(" ", "%20");
                search2 = search2.toLowerCase();
                System.out.println("search2: " + search2);
                String q2 = String.format("%sapp_id=%s&app_key=%s&nutrition-type=cooking&ingr=%s",
                    uri2, app_id2, app_key2, search2);
                System.out.println("url: " + q2);

                HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(q2)).build();
                HttpResponse<String> response2 = HTTP_CLIENT.send(request, BodyHandlers.ofString());

                String jsonString2 = response2.body();
                EdamamNutrition edamamNutrition = GSON.fromJson(jsonString2, EdamamNutrition.class);
                System.out.println("Carbs: " + edamamNutrition.totalNutrients);
            } // for


            System.out.println(edamamResponse.hits[0].recipe.label);

            changeScene(edamamResponse);


        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } // catch

    } // getRecipe


    private void changeScene(EdamamResponse response) {
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitHeight(100);
        backgroundImage.setFitWidth(600);


        int ingredientLength = response.hits[0].recipe.ingredientLines.length;
        int labelLength = response.hits[0].recipe.healthLabels.length;

        foodPic.setImage(new Image(response.hits[0].recipe.image));
        foodPic.setFitWidth(150);
        foodPic.setFitHeight(150);
        foodPic.setSmooth(true);

        String yieldString = Integer.toString(response.hits[0].recipe.yield);
        yield.setText("Servings: " + yieldString);
        for (int i = 0; i < labelLength; i++) {
            String labelLine = response.hits[0].recipe.healthLabels[i];
            labelLine.replaceAll("-", " ");
            labels.setText(labels.getText() + labelLine + " * ");
        } // for

        recipeName.setText(response.hits[0].recipe.label);
        recipeName.setFont(new Font(18));

        ingredients.setWrapText(true);
        ingredients.setTextAlignment(TextAlignment.JUSTIFY);
        ingredients.setMaxWidth(300);

        ingredients.setText("\tIngredients:\n");
        for (int i = 0; i < ingredientLength; i++) {
            String ingrLine = response.hits[0].recipe.ingredientLines[i];
            ingredients.setText(ingredients.getText() + "- " + ingrLine + "\n");
        } // for

    } // changeScene

} // ApiApp
