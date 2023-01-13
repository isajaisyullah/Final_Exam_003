/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws.FINAL_EXAM.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.FINAL_EXAM.exceptions.NonexistentEntityException;
import ws.FINAL_EXAM.exceptions.PreexistingEntityException;

/**
 *
 * @author ACER SWIFT3
 */
public class SuratJpaController implements Serializable {

    public SuratJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ws_FINAL_EXAM_jar_0.0.1-SNAPSHOTPU");
    
    public SuratJpaController(){}

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Surat surat) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(surat);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSurat(surat.getId()) != null) {
                throw new PreexistingEntityException("Surat " + surat + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Surat surat) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            surat = em.merge(surat);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = surat.getId();
                if (findSurat(id) == null) {
                    throw new NonexistentEntityException("The surat with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Surat surat;
            try {
                surat = em.getReference(Surat.class, id);
                surat.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The surat with id " + id + " no longer exists.", enfe);
            }
            em.remove(surat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Surat> findSuratEntities() {
        return findSuratEntities(true, -1, -1);
    }

    public List<Surat> findSuratEntities(int maxResults, int firstResult) {
        return findSuratEntities(false, maxResults, firstResult);
    }

    private List<Surat> findSuratEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Surat.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Surat findSurat(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Surat.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuratCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Surat> rt = cq.from(Surat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
