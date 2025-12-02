package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.entity.Cita;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void enviarConfirmacionCita(Cita cita, String emailPaciente, String emailFisioterapeuta) {
        try {
            // Enviar correo al paciente
            enviarCorreoPaciente(cita, emailPaciente);

            // Enviar correo al fisioterapeuta
            enviarCorreoFisioterapeuta(cita, emailFisioterapeuta);

            log.info("Correos de confirmación enviados para la cita: {}", cita.getCodigo());
        } catch (Exception e) {
            log.error("Error al enviar correos de confirmación: {}", e.getMessage());
        }
    }

    private void enviarCorreoPaciente(Cita cita, String emailPaciente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailPaciente);
        message.setSubject("Confirmación de Cita - Centro de Fisioterapia Santa María de Asís");

        String contenido = String.format(
            "Estimado/a %s,\n\n" +
            "Su cita ha sido confirmada con los siguientes detalles:\n\n" +
            "Código de Cita: %s\n" +
            "Fisioterapeuta: %s\n" +
            "Especialidad: %s\n" +
            "Fecha: %s\n" +
            "Hora: %s\n" +
            "Ubicación: %s\n" +
            "%s\n\n" +
            "Por favor, llegue 10 minutos antes de su cita.\n\n" +
            "Si necesita cancelar o reprogramar, contáctenos con anticipación.\n\n" +
            "Saludos cordiales,\n" +
            "Centro de Fisioterapia Santa María de Asís",
            cita.getPaciente().getUser().getFirstName() + " " + cita.getPaciente().getUser().getLastName(),
            cita.getCodigo(),
            cita.getFisioterapeuta().getUser().getFirstName() + " " + cita.getFisioterapeuta().getUser().getLastName(),
            cita.getFisioterapeuta().getEspecialidad(),
            cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
            cita.getUbicacion(),
            cita.getMotivo() != null ? "Motivo: " + cita.getMotivo() : ""
        );

        message.setText(contenido);
        mailSender.send(message);
        log.info("Correo enviado al paciente: {}", emailPaciente);
    }

    private void enviarCorreoFisioterapeuta(Cita cita, String emailFisioterapeuta) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailFisioterapeuta);
        message.setSubject("Nueva Cita Asignada - Centro de Fisioterapia Santa María de Asís");

        String contenido = String.format(
            "Estimado/a Dr./Dra. %s,\n\n" +
            "Se le ha asignado una nueva cita:\n\n" +
            "Código de Cita: %s\n" +
            "Paciente: %s (%s)\n" +
            "Fecha: %s\n" +
            "Hora: %s\n" +
            "Ubicación: %s\n" +
            "%s\n\n" +
            "Saludos cordiales,\n" +
            "Centro de Fisioterapia Santa María de Asís",
            cita.getFisioterapeuta().getUser().getFirstName() + " " + cita.getFisioterapeuta().getUser().getLastName(),
            cita.getCodigo(),
            cita.getPaciente().getUser().getFirstName() + " " + cita.getPaciente().getUser().getLastName(),
            cita.getPaciente().getCodigo(),
            cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
            cita.getUbicacion(),
            cita.getMotivo() != null ? "Motivo de consulta: " + cita.getMotivo() : ""
        );

        message.setText(contenido);
        mailSender.send(message);
        log.info("Correo enviado al fisioterapeuta: {}", emailFisioterapeuta);
    }

    @Async
    public void enviarNotificacionCancelacion(Cita cita, String emailPaciente, String emailFisioterapeuta) {
        try {
            // Correo al paciente
            SimpleMailMessage messagePaciente = new SimpleMailMessage();
            messagePaciente.setTo(emailPaciente);
            messagePaciente.setSubject("Cita Cancelada - Centro de Fisioterapia Santa María de Asís");
            messagePaciente.setText(String.format(
                "Estimado/a %s,\n\n" +
                "Su cita con código %s programada para el %s a las %s ha sido cancelada.\n\n" +
                "Si desea reprogramar, por favor contáctenos.\n\n" +
                "Saludos cordiales,\n" +
                "Centro de Fisioterapia Santa María de Asís",
                cita.getPaciente().getUser().getFirstName(),
                cita.getCodigo(),
                cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm"))
            ));
            mailSender.send(messagePaciente);

            // Correo al fisioterapeuta
            SimpleMailMessage messageFisio = new SimpleMailMessage();
            messageFisio.setTo(emailFisioterapeuta);
            messageFisio.setSubject("Cita Cancelada - Centro de Fisioterapia Santa María de Asís");
            messageFisio.setText(String.format(
                "Estimado/a Dr./Dra. %s,\n\n" +
                "La cita con código %s del paciente %s programada para el %s a las %s ha sido cancelada.\n\n" +
                "Saludos cordiales,\n" +
                "Centro de Fisioterapia Santa María de Asís",
                cita.getFisioterapeuta().getUser().getFirstName(),
                cita.getCodigo(),
                cita.getPaciente().getUser().getFirstName() + " " + cita.getPaciente().getUser().getLastName(),
                cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm"))
            ));
            mailSender.send(messageFisio);

            log.info("Correos de cancelación enviados para la cita: {}", cita.getCodigo());
        } catch (Exception e) {
            log.error("Error al enviar correos de cancelación: {}", e.getMessage());
        }
    }

    @Async
    public void enviarEmailRecuperacionPassword(String email, String nombre, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperación de Contraseña - Centro de Fisioterapia Santa María");

            String resetLink = "http://localhost:3000/reset-password?token=" + token;

            String contenido = String.format(
                "Hola %s,\n\n" +
                "Hemos recibido una solicitud para restablecer tu contraseña.\n\n" +
                "Para crear una nueva contraseña, haz clic en el siguiente enlace:\n" +
                "%s\n\n" +
                "Este enlace expirará en 24 horas.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                "Saludos cordiales,\n" +
                "Centro de Fisioterapia Santa María de Asís",
                nombre,
                resetLink
            );

            message.setText(contenido);
            mailSender.send(message);
            log.info("Correo de recuperación enviado a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar correo de recuperación: {}", e.getMessage());
        }
    }

    @Async
    public void enviarCodigoRecuperacionPassword(String email, String nombre, String codigo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Código de Recuperación - Centro de Fisioterapia Santa María");

            String contenido = String.format(
                "Hola %s,\n\n" +
                "Hemos recibido una solicitud para restablecer tu contraseña.\n\n" +
                "Tu código de verificación es:\n\n" +
                "    %s\n\n" +
                "Este código expirará en 15 minutos.\n\n" +
                "Ingresa este código en la aplicación para continuar con el cambio de contraseña.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo y tu contraseña permanecerá sin cambios.\n\n" +
                "Por seguridad, no compartas este código con nadie.\n\n" +
                "Saludos cordiales,\n" +
                "Centro de Fisioterapia Santa María de Asís",
                nombre,
                codigo
            );

            message.setText(contenido);
            mailSender.send(message);
            log.info("Código de recuperación enviado a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar código de recuperación: {}", e.getMessage());
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }
}
