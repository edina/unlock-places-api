package edina.geocrosswalk.web.ws;


/**
 * Command object for <code>DistanceController</code>.
 * 
 * 
 */
public class DistanceCommand extends AbstractGXWCommand {
	
	private Integer idA;
	private Integer idB;
	
    public Integer getIdA() {
        return idA;
    }
    public void setIdA(Integer idA) {
        this.idA = idA;
    }
    public Integer getIdB() {
        return idB;
    }
    public void setIdB(Integer idB) {
        this.idB = idB;
    }

}