package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class MainSkill {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger log = Logger.getLogger(MainSkill.class);
	
	@ManyToOne
	private StandardizedRole role;
	
	@ManyToOne 
	private Skill skill;
	
	public static List<MainSkill> findMainSkillEntriesByRoleID(long value,int start,int length)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MainSkill m WHERE m.role.id = " + value;
		TypedQuery<MainSkill> q = em.createQuery(s, MainSkill.class);
		q.setFirstResult(start);
    	q.setMaxResults(length);
		return q.getResultList();
	}

	public static long countMainSkillEntriesByRoleID(long value)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MainSkill m WHERE m.role.id = " + value;
		TypedQuery<MainSkill> q = em.createQuery(s, MainSkill.class);
		java.util.List<MainSkill> result = q.getResultList();
		
		return result.size();
		//return q.getResultList().size();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Role: ").append(getRole()).append(", ");
        sb.append("Skill: ").append(getSkill()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            MainSkill attached = MainSkill.findMainSkill(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public MainSkill merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MainSkill merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new MainSkill().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMainSkills() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MainSkill o", Long.class).getSingleResult();
    }

	public static List<MainSkill> findAllMainSkills() {
        return entityManager().createQuery("SELECT o FROM MainSkill o", MainSkill.class).getResultList();
    }

	public static MainSkill findMainSkill(Long id) {
        if (id == null) return null;
        return entityManager().find(MainSkill.class, id);
    }

	public static List<MainSkill> findMainSkillEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MainSkill o", MainSkill.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public StandardizedRole getRole() {
        return this.role;
    }

	public void setRole(StandardizedRole role) {
        this.role = role;
    }

	public Skill getSkill() {
        return this.skill;
    }

	public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
