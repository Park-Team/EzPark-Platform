package com.acme.ezpark.platform.payment.interfaces.rest;

import com.acme.ezpark.platform.payment.domain.model.queries.GetPaymentCardsByUserIdQuery;
import com.acme.ezpark.platform.payment.domain.model.queries.GetPaymentCardByIdQuery;
import com.acme.ezpark.platform.payment.domain.services.PaymentCardCommandService;
import com.acme.ezpark.platform.payment.domain.services.PaymentCardQueryService;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.CreatePaymentCardResource;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.PaymentCardResource;
import com.acme.ezpark.platform.payment.interfaces.rest.resources.UpdatePaymentCardResource;
import com.acme.ezpark.platform.payment.interfaces.rest.transform.CreatePaymentCardCommandFromResourceAssembler;
import com.acme.ezpark.platform.payment.interfaces.rest.transform.PaymentCardResourceFromEntityAssembler;
import com.acme.ezpark.platform.payment.interfaces.rest.transform.UpdatePaymentCardCommandFromResourceAssembler;
import com.acme.ezpark.platform.payment.interfaces.rest.transform.SetDefaultPaymentCardCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/payment-cards", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment Cards", description = "Payment Card Management Endpoints")
public class PaymentCardsController {

    private final PaymentCardCommandService paymentCardCommandService;
    private final PaymentCardQueryService paymentCardQueryService;

    public PaymentCardsController(PaymentCardCommandService paymentCardCommandService,
                                 PaymentCardQueryService paymentCardQueryService) {
        this.paymentCardCommandService = paymentCardCommandService;
        this.paymentCardQueryService = paymentCardQueryService;
    }

    @PostMapping
    @Operation(summary = "Create payment card", description = "Create a new payment card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment card created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<PaymentCardResource> createPaymentCard(@RequestBody CreatePaymentCardResource resource) {
        var createPaymentCardCommand = CreatePaymentCardCommandFromResourceAssembler.toCommandFromResource(resource);
        var paymentCard = paymentCardCommandService.handle(createPaymentCardCommand);
        if (paymentCard.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var paymentCardResource = PaymentCardResourceFromEntityAssembler.toResourceFromEntity(paymentCard.get());
        return new ResponseEntity<>(paymentCardResource, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentCardId}")
    @Operation(summary = "Get payment card by id", description = "Get payment card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment card found"),
            @ApiResponse(responseCode = "404", description = "Payment card not found")
    })
    public ResponseEntity<PaymentCardResource> getPaymentCardById(@PathVariable Long paymentCardId) {
        var getPaymentCardByIdQuery = new GetPaymentCardByIdQuery(paymentCardId);
        var paymentCard = paymentCardQueryService.handle(getPaymentCardByIdQuery);
        if (paymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var paymentCardResource = PaymentCardResourceFromEntityAssembler.toResourceFromEntity(paymentCard.get());
        return ResponseEntity.ok(paymentCardResource);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all payment cards by user id", description = "Get all payment cards by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment cards found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })    public ResponseEntity<List<PaymentCardResource>> getAllPaymentCardsByUserId(@PathVariable Long userId) {
        var getAllPaymentCardsByUserIdQuery = new GetPaymentCardsByUserIdQuery(userId);
        var paymentCards = paymentCardQueryService.handle(getAllPaymentCardsByUserIdQuery);
        var paymentCardResources = paymentCards.stream()
                .map(PaymentCardResourceFromEntityAssembler::toResourceFromEntity)
                .toList();        return ResponseEntity.ok(paymentCardResources);
    }

    @PutMapping("/{paymentCardId}")
    @Operation(summary = "Update payment card", description = "Update payment card information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment card updated"),
            @ApiResponse(responseCode = "404", description = "Payment card not found")
    })
    public ResponseEntity<PaymentCardResource> updatePaymentCard(
            @PathVariable Long paymentCardId,
            @RequestBody UpdatePaymentCardResource resource) {
        var updatePaymentCardCommand = UpdatePaymentCardCommandFromResourceAssembler.toCommandFromResource(paymentCardId, resource);
        var paymentCard = paymentCardCommandService.handle(updatePaymentCardCommand);
        
        if (paymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var paymentCardResource = PaymentCardResourceFromEntityAssembler.toResourceFromEntity(paymentCard.get());
        return ResponseEntity.ok(paymentCardResource);
    }    @PutMapping("/{paymentCardId}/default")
    @Operation(
        summary = "Set payment card as default", 
        description = "Marks the specified payment card as the default card for the user. " +
                     "This action automatically unsets any previously default card for the same user.",
        tags = {"Payment Cards"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Payment card successfully set as default",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PaymentCardResource.class)
                )
            ),
            @ApiResponse(
                responseCode = "404", 
                description = "Payment card not found with the provided ID",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value = "{\"timestamp\":\"2025-06-12T17:30:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Payment card not found with ID: 123\"}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Invalid payment card ID provided",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value = "{\"timestamp\":\"2025-06-12T17:30:00\",\"status\":400,\"error\":\"Bad Request\",\"message\":\"Payment card ID must be a positive number\"}"
                    )
                )
            )
    })
    public ResponseEntity<PaymentCardResource> setDefaultPaymentCard(
            @PathVariable 
            @io.swagger.v3.oas.annotations.Parameter(
                description = "Unique identifier of the payment card to set as default",
                example = "123",
                required = true
            )
            Long paymentCardId) {
        
        // Validate input
        if (paymentCardId == null || paymentCardId <= 0) {
            throw new com.acme.ezpark.platform.shared.domain.exceptions.ValidationException(
                "Payment card ID must be a positive number"
            );
        }
        
        var setDefaultPaymentCardCommand = SetDefaultPaymentCardCommandFromResourceAssembler.toCommandFromResource(paymentCardId);
        var paymentCard = paymentCardCommandService.handle(setDefaultPaymentCardCommand);
        
        if (paymentCard.isEmpty()) {
            throw new com.acme.ezpark.platform.shared.domain.exceptions.ResourceNotFoundException(
                "Payment card not found with ID: " + paymentCardId
            );
        }
        
        var paymentCardResource = PaymentCardResourceFromEntityAssembler.toResourceFromEntity(paymentCard.get());
        return ResponseEntity.ok(paymentCardResource);
    }

    @DeleteMapping("/{paymentCardId}")
    @Operation(summary = "Delete payment card", description = "Delete payment card by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment card deleted"),
            @ApiResponse(responseCode = "404", description = "Payment card not found")
    })
    public ResponseEntity<?> deletePaymentCard(@PathVariable Long paymentCardId) {
        var getPaymentCardByIdQuery = new GetPaymentCardByIdQuery(paymentCardId);
        var paymentCard = paymentCardQueryService.handle(getPaymentCardByIdQuery);
        if (paymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        paymentCardCommandService.deletePaymentCard(paymentCardId);
        return ResponseEntity.ok("Payment card with given id successfully deleted");
    }
}
