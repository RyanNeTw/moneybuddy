package moneybuddy.fr.moneybuddy.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

    @Value("${resend.mail}")
    private String fromEmail;

    @Value("${resend.api_key}")
    private String apiKey;

    public String sendEmail(String to, String subject, String htmlContent) {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(to)
                .subject(subject)
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            return "Email envoyé avec ID : " + response.getId();
        } catch (ResendException e) {
            throw new RuntimeException("Échec d'envoi de l'email : " + e.getMessage(), e);
        }
    }
}