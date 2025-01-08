# Proyect-Record-Backend
Desarrollo de Backend para la Plataforma Web de Gestión de Pedidos en Multiservicios Recort

En este proyecto backend, se esta utilizando:
  -Java Springboot e Hibernate Jpa.
  -Validaciones y DTO.
  -Mensajes de Errores y Respuestas Estandarizadas.
  -Anotaciones Personalizadas.
  -Seguridad con Spring Security y JWT (Libreria JJwt).
  -Proteccion de endpoints.
  -Envios automatizados de correos electronicos y autenticacion de correo electronico.
  -Envio automatizado de mensajes de WhatsApp por medio de Twilio.
  -Documentacion con swagger (Proximamente version 1.1.0).

Para la configuracion de este proyecto y uso necesitas realizar los siguientes pasos:
-En cada Controlador ubicado en el archivo Controllers, cambiar @CrossOrigin(origins = "http://localhost:8081",originPatterns = "*") por la direccion a la cual se va a conectar el ApiRest.
-En los archivos properties ubicados en la carpeta Resources:
  application.properties -> Ingresar el username y password correspondientes a tu instancia MySql (Asi como en spring.datasource.url asegurate de poner tu ruta                                             correspondiente), las tablas se van a crear al momento de inicializar el proyecto, luego de esto eliminar (spring.jpa.hibernate.ddl-auto=create)                                si quieres conservar los cambios y que no se reinicie las tablas de la base de datos creada.
  email.properties -> En este caso "email.username" debe contener la direccion de correo electronico que deceas usar para el envio de notificaciones, "email.password" debe                           contener una contraseña generica, esta se optiene dirigiendote al administrador de cuenta de tu correo, buscar contraseñas de aplicaciones, colocas un                          nombre y se te entrega la contraseña (Esto solo si la cuenta tiene verificacion de 2 pasos activa).
  WhatsApp.properties -> Aqui, es necesario contar con una cuenta de Twilio (Disponibilidad de envio de mensajes limitada en el espacio gratuito), para asegurar que Twilio                              pueda hacer llegar sus mensajes, el que lo tiene que recibir le tiene que escribir "join out-grade".
