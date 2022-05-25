package fr.funixgaming.api.server;

import fr.funixgaming.api.client.config.FunixApiConfig;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.enums.LineCommand;
import fr.funixgaming.api.server.mail.services.FunixMailService;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineApp implements CommandLineRunner {

    private final UserService userService;
    private final FunixMailService mailService;
    private final FunixApiConfig apiConfig;

    @Override
    public void run(String... args) {
        try {
            if (args.length >= 2) {
                final String command = args[0];

                if (command.equalsIgnoreCase("help")) {
                    sendHelp();
                } else if (command.equalsIgnoreCase(LineCommand.GET_API_USER.getCommand())) {
                    getApiInfos();
                } else {
                    throw new ApiBadRequestException(String.format("La commande entrée %s est incorecte. Veuillez entrer help pour avoir la liste des commandes.", command));
                }
            }
        } catch (Exception e) {
            log.error("Erreur {}", e.getMessage());
        }
    }

    private void sendHelp() {
        log.info("Liste des commandes serveur:");

        for (final LineCommand lineCommand : LineCommand.values()) {
            log.info("Commande: {} Info: {} Usage: {}", lineCommand.getCommand(), lineCommand.getDescription(), lineCommand.getExample());
        }
    }

    private void getApiInfos() {
        final User user = this.userService.getOrCreateApiUser();
        final FunixMailDTO mailDTO = new FunixMailDTO();

        mailDTO.setFrom("admin@funixgaming.fr");
        mailDTO.setTo(apiConfig.getEmail());
        mailDTO.setSubject("[FunixAPI] Identifiants pour le login api.");
        mailDTO.setText(String.format("username: %s password: %s", user.getUsername(), user.getPassword()));

        mailService.addMail(mailDTO);
        log.info("Envoi de la requête pour récupérer les infos api par mail.");
    }
}
