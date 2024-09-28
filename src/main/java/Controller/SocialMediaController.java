package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.AccountService;
import java.util.List;
import java.util.Map;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    // SocialMediaController Constructor
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        //User Registration
        app.post("/register", this::postUserRegistrationHandler);
        
        // User Login
        app.post("/login", this::postLoginHandler);
        
        // Create Message
        app.post("/messages", this::postNewMessageHandler);
        
        // Get All Messages
        app.get("/messages", this::getAllMessagesHandler);
        
        // Get One Message
        app.get("/messages/{message_id}", this::getMessageHandler);

        // Delete one Message
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        // Update Existing Message
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // Get All Messages from User
        app.get("/accounts/{account_id}/messages", this::getAllUserMessagesHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // User Registration Post Handler
    private void postUserRegistrationHandler(Context ctx) throws JsonProcessingException {
        // Map HTTP Body to Account Object
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        // Persist Account
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            // Successful: Return Added Account (including account_id)
            ctx.json(mapper.writeValueAsString(accountService.getAccountByUserName(addedAccount)));
            ctx.status(200);
        }else{
            // Unsuccessful (Account Already Exists)
            ctx.status(400);
        }
    }

    // User Login Post Handler
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        // Map HTTP Body to Account Object
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        // Authenticate
        Account loggedInAccount = accountService.login(account);
        if(loggedInAccount != null){
            // Successful: Return Added Account (including account_id)
            ctx.json(mapper.writeValueAsString(accountService.getAccountByUserName(loggedInAccount)));
            ctx.status(200);
        }else{
            // Unauthorized
            ctx.status(401);
        }
    }

    private void postNewMessageHandler(Context ctx) throws JsonProcessingException{
        // Map HTTP Body to Account Object
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        // Persist Message
        Message newMessage = messageService.newMessage(message);
        if(newMessage != null){
            // Successful: Return New Message (including message_id)
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        }else{
            // Unsuccessful
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ObjectMapper mapper = new ObjectMapper();

        ctx.json(mapper.writeValueAsString(messages));
        ctx.status(200);
    }

    private void getMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");

        Message selectMessage = messageService.getMessage(message_id);
        if(selectMessage != null){
            ctx.json(mapper.writeValueAsString(selectMessage));
        }
        ctx.status(200);
    }
    
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");

        Message deletedMessage = messageService.deleteMessage(message_id);
        if(deletedMessage != null){
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
        ctx.status(200);
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        Message updatedMessage = messageService.updateMessage(message_id, message.getMessage_text());
        if (updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllUserMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String account_id = ctx.pathParam("account_id");
        List<Message> messages = messageService.getAllUserMessages(account_id);
        ctx.json(mapper.writeValueAsString(messages));
        ctx.status(200);
    }
    
}