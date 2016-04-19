/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g01.muu.main;

import g01.muu.enums.TipoUsuarioEnum;
import g01.muu.models.TipoUsuario;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class created for the persistence management of the "cows"
 * @author Sergio
 */
public class PersistenceManager {

    public static final boolean DEBUG = true;

    private static final PersistenceManager singleton = new PersistenceManager();

    protected EntityManagerFactory emf;

    public static PersistenceManager getInstance() {

        return singleton;
    }
    

    private PersistenceManager() {
    }

    public EntityManagerFactory getEntityManagerFactory() {

        if (emf == null) {
            createEntityManagerFactory();
        }
        return emf;
    }

    public void closeEntityManagerFactory() {

        if (emf != null) {
            emf.close();
            emf = null;
            if (DEBUG) {
                System.out.println("n*** Persistence finished at " + new java.util.Date());
            }
        }
    }

    protected void createEntityManagerFactory() {

        this.emf = Persistence.createEntityManagerFactory("MuuPU", System.getProperties());
        if (DEBUG) {
            System.out.println("n*** Persistence started at " + new java.util.Date());
        }
    }
    
    
    
    
}
