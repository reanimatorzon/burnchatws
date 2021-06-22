package com.heroku.burnchatws.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.stereotype.Controller
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.time.LocalDateTime


@SpringBootApplication
class BurnchatwsApplication

fun main(args: Array<String>) {
    runApplication<BurnchatwsApplication>(*args)
}

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.setApplicationDestinationPrefixes("/app")
        config.enableSimpleBroker("/channel")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat")
        registry.addEndpoint("/chat").withSockJS()
    }

}

@Controller
class WebSocketController {

    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations

    @MessageMapping("/chat/{roomId}")
    fun send(@DestinationVariable roomId: String, @Payload message: Message) {
        messagingTemplate.convertAndSend(
            "/channel/$roomId",
            OutputMessage(message.from, message.to, message.text, LocalDateTime.now())
        )
    }

}