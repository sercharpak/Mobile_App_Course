/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g01.muu.services;


import g01.muu.enums.TipoUsuarioEnum;
import g01.muu.main.PersistenceManager;
import g01.muu.models.Vaca;
import g01.muu.models.VacaDTO;
import g01.muu.models.TipoUsuario;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.simple.JSONObject;

/**
 * Class created to manage the services for the "cows"
 * @author Sergio
 */
@Path("/muu")
@Produces(MediaType.APPLICATION_JSON)
public class MuuServices {

    @PersistenceContext(unitName = "MuuPU")
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/vacas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Query q = entityManager.createQuery("select u from vaca u order by u.nombre ASC");
        List<Vaca> vacas = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vacas).build();
    }

    @POST
    @Path("/vacas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVaca(Vaca vaca)
    {

        Vaca v = new Vaca();
        JSONObject rta = new JSONObject();
        v.setNombre(vaca.getNombre());
        v.setDescripcion(vaca.getDescripcion());


        try {
            entityManager.getTransaction().begin();
            entityManager.persist(v);
            entityManager.getTransaction().commit();
            entityManager.refresh(v);
            rta.put("vaca_id", v.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            v = null;
        } finally {
        	entityManager.clear();
        	entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
    }


    @GET
    @Path("/tiposUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTiposUsuario() {

        Query q = entityManager.createQuery("SELECT t FROM tipousuario t order by t.username ASC");
        List<TipoUsuario> tiposusuarios = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(tiposusuarios).build();
    }
/*
    @POST
    @Path("/tiposUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTipoUsuario(TipoUsuario tipoUsuario)
    {
      Query q = entityManager.createQuery("select u from tipousuario u order by u.nombre ASC where u.username=''"+tipoUsuario.getUsername().toString()+"''");
      List<TipoUsuario> usuarios = q.getResultList();
      if(usuarios.isEmpty()){
        TipoUsuario u = new TipoUsuario();
        JSONObject rta = new JSONObject();
        u.setTipo(tipoUsuario.getTipo());
        u.setUsername(tipoUsuario.getUsername());
        u.setLatitude(tipoUsuario.getLatitude());
        u.setLongitude(tipoUsuario.getLongitude());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(u);
            entityManager.getTransaction().commit();
            entityManager.refresh(u);
            rta.put("tipoID_id", u.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            u = null;
        } finally {
        	entityManager.clear();
        	entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
      }
      else{
        TipoUsuario u = usuarios.get(0);
        JSONObject rta = new JSONObject();
        u.setLatitude(tipoUsuario.getLatitude());
        u.setLongitude(tipoUsuario.getLongitude());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(u);
            entityManager.getTransaction().commit();
            entityManager.refresh(u);
            rta.put("tipoID_id", u.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            u = null;
        } finally {
          entityManager.clear();
          entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
      }
    }
    */
    @POST
    @Path("/tiposUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTipoUsuario(TipoUsuario tipoUsuario)
    {

        TipoUsuario u = new TipoUsuario();
        JSONObject rta = new JSONObject();
        u.setTipo(tipoUsuario.getTipo());
        u.setUsername(tipoUsuario.getUsername());
        u.setLatitude(tipoUsuario.getLatitude());
        u.setLongitude(tipoUsuario.getLongitude());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(u);
            entityManager.getTransaction().commit();
            entityManager.refresh(u);
            rta.put("tipousuario_id", u.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            u = null;
        } finally {
          entityManager.clear();
          entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();

    }



    @GET
    @Path("/poblarTipoesUsuario")
    public Response poblarTiposUsuario()
    {
        JSONObject rta = new JSONObject();
        TipoUsuarioEnum[] tipos = TipoUsuarioEnum.values();
        for (int i = 0; i < tipos.length; i++)
        {
            TipoUsuario m = new TipoUsuario();
            m.setTipo(tipos[i].toString());

            try
            {
                Query q = entityManager.createQuery("select u from tipousuario u  where u.tipo = '" + tipos[i].toString() + "'");
                if (q.getResultList().isEmpty())
                {
                    rta.put("ADD " + i, tipos[i].toString());
                    entityManager.persist(m);
                }
            }
            catch (Exception e)
            {
                rta.put("ERROR " + i, i + " : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();

    }


    @OPTIONS
    public Response cors(@javax.ws.rs.core.Context HttpHeaders requestHeaders) {
        return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept").build();
    }




}
