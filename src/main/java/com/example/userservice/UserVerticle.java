package com.example.userservice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.json.JsonObject;



public class UserVerticle extends AbstractVerticle {
    private final UserService userService;

    public UserVerticle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/users").handler(this::handleCreateUser);
        router.get("/users/:id").handler(this::handleGetUser);
        router.put("/users/:id/email").handler(this::handleUpdateEmail);
        router.delete("/users/:id").handler(this::handleDeleteUser);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888)
                .onSuccess(server -> {
                    System.out.println("HTTP server started on port " + server.actualPort());
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);


    }

    private void handleCreateUser(RoutingContext ctx) {
        JsonObject json = ctx.body().asJsonObject();
        String name = json.getString("name");
        String email = json.getString("email");

        if (name == null || email == null) {
            ctx.response().setStatusCode(400).end("Missing name or email");
            return;
        }

        User user = userService.createUser(name, email);
        JsonObject jsonUser = new JsonObject()
                .put("id", user.getId())
                .put("name", user.getName())
                .put("email", user.getEmail());
        ctx.response().putHeader("Content-Type", "application/json")
                .end(jsonUser.encode());
    }

    private void handleGetUser(RoutingContext ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            User user = userService.getUser(id);
            JsonObject jsonUser = new JsonObject()
                    .put("id", user.getId())
                    .put("name", user.getName())
                    .put("email", user.getEmail());
            ctx.response().putHeader("Content-Type", "application/json")
                    .end(jsonUser.encode());
        } catch (Exception e) {
            ctx.response().setStatusCode(404).end(e.getMessage());
        }
    }

    private void handleUpdateEmail(RoutingContext ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            String email = ctx.body().asJsonObject().getString("email");
            if (email == null) {
                ctx.response().setStatusCode(400).end("Missing email");
                return;
            }
            User user = userService.updateEmail(id, email);
            JsonObject jsonUser = new JsonObject()
                    .put("id", user.getId())
                    .put("name", user.getName())
                    .put("email", user.getEmail());
            ctx.response().putHeader("Content-Type", "application/json")
                    .end(jsonUser.encode());
        } catch (Exception e) {
            ctx.response().setStatusCode(404).end(e.getMessage());
        }
    }

    private void handleDeleteUser(RoutingContext ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            userService.deleteUser(id);
            ctx.response().setStatusCode(204).end();
        } catch (Exception e) {
            ctx.response().setStatusCode(404).end(e.getMessage());
        }
    }
}