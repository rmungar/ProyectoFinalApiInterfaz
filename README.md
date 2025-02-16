# **PROYECTO API + INTERFAZ**

## **Documentos**

###  Usuario

Documento donde vamos a almacenar la información sobre los usuarios de la base de datos.

| _**Campo**_   | **_Tipo_** | _**Descripción**_                                                                                       |
|---------------|------------|---------------------------------------------------------------------------------------------------------|
| **_id**       | String?    | Campo que va a actuar como la clave primaria de la tabla. Se iguala al correo del usuario al crear uno. |
| **username**  | String     | Nick del usuario.                                                                                       |
| **password**  | String     | Contraseña del usuario.                                                                                 |
| **email**     | String     | Email del usuario.                                                                                      |
| **direccion** | Direccion  | Dirección del usuario.                                                                                  |
| **rol**       | String?    | Rol del usuario.                                                                                        |

+ El campo `username` no puede estar vacío o ser nulo.
+ El campo `password` no puede estar vacío o ser nulo.
+ El campo `email`no puede estar vacío o ser nulo. Debe contener '@' y alguna terminación válida.
+ Dentro del campo `direccion` se comprobarán si la provincia y el municipio son correctos y si el municipio pertenece a la provincia ingresada.
+ El campo `rol` será asignado automaticamente a 'USER' si no se ingresa el valor 'ADMIN' o si el campo se deja vacío.

### Tarea

Documento donde vamos a almacenar la información sobre las tareas que se les asignan a los usuarios.

| _**Campo**_         | **_Tipo_** | **_Descripción_**                                                                                         |
|---------------------|------------|-----------------------------------------------------------------------------------------------------------|
| **_id**             | String?    | Campo que va a actuar como la clave primaria de la tabla. Se iguala a una mezcla de la fecha y el título. |
| **titulo**          | String     | Campo que resume la tarea.                                                                                |
| **estado**          | Boolean    | Campo que indica el estado de la tarea. True = Terminado, False =  En proceso.                            |
| **descripcion**     | String     | Campo que indica en que consiste la tarea.                                                                |
| **usuario**         | Usuario    | Campo que indica el usuario en posesión de esa tarea.                                                     |
| **fechaProgramada** | Date       | Fecha límite para la realización de la tarea.                                                             |

+ El campo `titulo` no puede estar vacío o ser nulo.
+ El campo `descripcion` no puede estar vacío o ser nulo.
+ El campo `usuario`no puede estar vacío o ser nulo. Debe ser un usuario registrado en la aplicación.
+ El campo `fechaProgramada` sera comprobado automáticamente para evitar asignaciones con fecha previa a la fecha del momento de creación. No puede ser un valor nulo.


## **Endpoints**


### Usuarios

+ _**GET**_ > **usuarios/login** > Endpoint que va a permitir a un usuario realizar un login en la aplicación. 
    
    Devolverá un token si las credenciales son correctas o una _BAD_REQUEST_ en caso contrario.
+ _**POST**_ > **usuarios/register** > Endpoint que va a permitir realizar un registro de un usuario pasado por el cuerpo de la petición en la aplicación. 

    Devolverá un _CREATED_ si la creación es realizada sin problemas o un _BAD_REQUEST_ si no.
+ _**UPDATE**_ > **usuarios/update** > Endpoint que va a permitir actualizar los campos de un usuario cuyo _id coincida con el valor pasado por el cuerpo de la petición. 

    Devuelve un _OK_ si la actualización es correcta o un _BAD_REQUEST_ si algún campo no es válido.
+ _**DELETE**_ > **usuarios/delete/{_id}** > Endpoint que permite eliminar a un usuario de la aplicación.
  
    Devolverá _OK_ en caso correcto o _BAD_REQUEST_ en caso de que no exista ningún usuario con ese _id.

### Tareas

+ _**POST**_ > **tareas/add/{_idUsuario}** > Endpoint que permite a un usuario con rol USER añadirse una tarea a sí mismo o a un usuario ADMIN añadirle una tarea a cualquier usuario.

    Devolverá un _CREATED_ en caso de que se cumplan todos los requisitos de lógica de negocio o un _BAD_REQUEST_ si no los cumple.
+ _**GET**_ > **tareas/obtener{_idUsuario}** > Endpoint que retorna todas las tareas creadas. Si el usuario es USER, solo verá las suyas y si el usuario es ADMIN, las verá todas.

    Devolverá un _OK_ si la petición es realizada sin problemas o un _NOT_FOUND_ si no existen tareas.
+ _**UPDATE**_ > **tareas/marcarTerminada/{_idTarea}** > Endpoint que va a permitir a un usuario USER marcar una tarea cuyo _id coincida con la proporcionada por parámetro como completada. O a un usuario ADMIN marcar cualquier tarea de cualquier usuario que coincida con el _id como completada. 

    Devuelve un _OK_ si la actualización es correcta o un _BAD_REQUEST_ si el usuario no posee esa tarea o no existe ninguna con ese _id.
+ _**DELETE**_ > **tareas/delete/{_idTarea}** > Endpoint que permite eliminar una tarea de un usuario. Si el usuario es USER, solo puede eliminar una tarea propia y si el usuario es ADMIN, puede borrar cualquier tarea de cualquier usuario.

    Devolverá _OK_ en caso correcto, _NOT_FOUND_ en caso de que no exista ninguna tarea con ese _id o _BAD_REQUEST_ si el usuario (en caso de que no sea ADMIN) intenta borrar una tarea que no le pertenece.