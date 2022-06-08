import java.util.*;


import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        port(5051); //default is 4567
        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/technology";

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
//            ArrayList<Post> posts = Post.getAll();
//            Date timeNow = Post.getDate();
//            model.put("posts", posts);
//            model.put("date", timeNow);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
