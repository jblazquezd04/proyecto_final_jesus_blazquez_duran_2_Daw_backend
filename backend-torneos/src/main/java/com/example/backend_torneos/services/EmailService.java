package com.example.backend_torneos.services;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${mail.from:MatchOn <noreply@matchon.app>}")
    private String from;

    @Value("${mail.admin:admin@matchon.app}")
    private String adminEmail;

    @Value("${mail.frontend-url:https://proyecto-final-jesus-blazquez-duran.vercel.app}")
    private String frontendUrl;

    // ── Bienvenida al registrarse ─────────────────────────────────

    @Async
    public void sendWelcome(String to, String username) {
        send(to,
             "¡Bienvenido a MatchOn!",
             buildHtml(
                 "¡Bienvenido, " + username + "!",
                 "Tu cuenta ha sido creada exitosamente. Ya puedes explorar torneos, unirte a equipos y competir contra otros jugadores.",
                 "Explorar torneos", frontendUrl + "/torneos"
             ));
    }

    // ── Alerta de inicio de sesión ────────────────────────────────

    @Async
    public void sendLoginAlert(String to, String username) {
        send(to,
             "Nuevo inicio de sesión en MatchOn",
             buildHtml(
                 "Hola, " + username,
                 "Hemos detectado un nuevo inicio de sesión en tu cuenta. Si no has sido tú, cambia tu contraseña inmediatamente desde tu perfil.",
                 "Ir a mi perfil", frontendUrl + "/perfil"
             ));
    }

    // ── Resultado de partida confirmado (jugadores) ───────────────

    @Async
    public void sendResultConfirmed(String to, String torneoNombre, String ganadorNombre) {
        send(to,
             "Resultado confirmado en " + torneoNombre,
             buildHtml(
                 "Resultado confirmado",
                 "El resultado de tu partida en <strong>" + torneoNombre + "</strong> ha sido confirmado.<br><br>"
                 + "Ganador: <strong>" + ganadorNombre + "</strong>",
                 "Ver el bracket", frontendUrl + "/torneos"
             ));
    }

    // ── Disputa pendiente (organizador) ──────────────────────────

    @Async
    public void sendDisputePending(String to, String torneoNombre) {
        send(to,
             "Disputa pendiente en " + torneoNombre,
             buildHtml(
                 "Disputa pendiente de resolución",
                 "Hay una discrepancia en los resultados reportados del torneo <strong>" + torneoNombre + "</strong>.<br><br>"
                 + "Accede al panel del torneo y resuelve la disputa para que el bracket pueda continuar.",
                 "Resolver disputa", frontendUrl + "/torneos"
             ));
    }

    // ── Confirmación de pago ──────────────────────────────────────

    @Async
    public void sendPaymentConfirmation(String to, String username, String orderId, String total) {
        send(to,
             "Pago confirmado – Pedido " + orderId,
             buildHtml(
                 "¡Pago confirmado, " + username + "!",
                 "Tu pedido <strong>" + orderId + "</strong> ha sido procesado correctamente.<br><br>"
                 + "Total: <strong>" + total + " €</strong><br><br>"
                 + "Gracias por confiar en MatchOn.",
                 "Ir a la tienda", frontendUrl + "/cart"
             ));
    }

    // ── Acuse de recibo del formulario de contacto (al usuario) ──

    @Async
    public void sendContactAck(String to, String nombre) {
        send(to,
             "Hemos recibido tu mensaje",
             buildHtml(
                 "Gracias, " + nombre,
                 "Hemos recibido tu mensaje correctamente. Nuestro equipo te responderá en un plazo de 24-48 horas.",
                 "Volver a MatchOn", frontendUrl
             ));
    }

    // ── Reenvío del mensaje al administrador ─────────────────────

    @Async
    public void forwardContact(String nombre, String replyTo, String mensaje) {
        String html = "<div style='font-family:Arial,sans-serif;padding:24px;'>"
                + "<h2 style='color:#4a2fdb;'>Nuevo mensaje de contacto</h2>"
                + "<p><strong>Nombre:</strong> " + nombre + "</p>"
                + "<p><strong>Email:</strong> <a href='mailto:" + replyTo + "'>" + replyTo + "</a></p>"
                + "<p><strong>Mensaje:</strong></p>"
                + "<div style='background:#f5f5f5;padding:14px;border-radius:8px;line-height:1.6;'>"
                + mensaje.replace("\n", "<br>")
                + "</div></div>";
        send(adminEmail, "Contacto de " + nombre + " – MatchOn", html);
    }

    // ── Envío genérico ────────────────────────────────────────────

    private void send(String to, String subject, String html) {
        if (mailSender == null) {
            log.warn("JavaMailSender no configurado – email omitido para {}: {}", to, subject);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Email enviado a {}: {}", to, subject);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }
    }

    // ── Plantilla HTML ────────────────────────────────────────────

    private String buildHtml(String title, String body, String btnText, String btnUrl) {
        return "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#0e0828;font-family:Arial,sans-serif;'>"
             + "<table width='100%' cellpadding='0' cellspacing='0'><tr><td align='center' style='padding:40px 16px;'>"
             + "<table width='560' cellpadding='0' cellspacing='0' style='background:#1d1358;border-radius:12px;overflow:hidden;max-width:560px;width:100%;'>"
             + "<tr><td style='background:#4a2fdb;padding:20px 32px;'>"
             + "<span style='color:#ffc000;font-size:1.3rem;font-weight:bold;letter-spacing:0.06em;'>⚡ MatchOn</span>"
             + "</td></tr>"
             + "<tr><td style='padding:32px;'>"
             + "<h2 style='color:#ffc000;margin:0 0 16px;font-size:1.15rem;font-weight:600;'>" + title + "</h2>"
             + "<p style='color:#c8d0f0;line-height:1.7;margin:0 0 28px;font-size:0.95rem;'>" + body + "</p>"
             + "<a href='" + btnUrl + "' style='display:inline-block;background:#ffc000;color:#000;"
             + "padding:12px 28px;border-radius:8px;text-decoration:none;font-weight:bold;font-size:0.88rem;'>"
             + btnText + "</a>"
             + "</td></tr>"
             + "<tr><td style='padding:14px 32px;border-top:1px solid rgba(74,47,219,0.3);'>"
             + "<p style='color:#445588;font-size:0.72rem;margin:0;'>Este es un mensaje automático de MatchOn. Por favor, no respondas a este correo.</p>"
             + "</td></tr>"
             + "</table></td></tr></table></body></html>";
    }
}
