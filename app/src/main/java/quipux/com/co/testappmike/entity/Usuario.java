package quipux.com.co.testappmike.entity;

import com.orhanobut.logger.Logger;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by home on 2/1/17.
 */

public class Usuario extends RealmObject {
  @Required private String nombre;
  private String apellido;
  @Required private String correoEletronico;
  @Required private String usuario; //unico
  @PrimaryKey private int documento; // llave primaria
  @Required private String contrasena; // al crearlo se asigna una temporal
  @Required private String fechaNacimiento;  // formato  dd/MM/yyyy
  @Required private String estado;
  @Required private String rol; // admin o usario

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getCorreoEletronico() {
    return correoEletronico;
  }

  public void setCorreoEletronico(String correoEletronico) {
    this.correoEletronico = correoEletronico;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public int getDocumento() {
    return documento;
  }

  public void setDocumento(int documento) {
    this.documento = documento;
  }

  public String getContrasena() {
    return contrasena;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }

  public String getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  @Override public String toString() {
    JSONObject json = new JSONObject();
    try {
      json.put("nombre", getNombre());
      json.put("apellido", getApellido());
      json.put("correoEletronico", getCorreoEletronico());
      json.put("fechaNacimiento", getFechaNacimiento());
      json.put("documento", getDocumento());
      json.put("rol", getRol());
      json.put("estado", getEstado());
      json.put("contrasena", getContrasena());
      json.put("usuario", getUsuario());
    } catch (JSONException e) {
      Logger.e(e.getMessage(), e);
    }
    return json.toString();
  }
}
