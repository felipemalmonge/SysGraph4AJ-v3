package cfg.model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.IElement;
import model.SysMethod;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.ReturnInstruction;

import cfg.processing.CFGBuilder;
import cfg.processing.CFGProcessor;

/**
 * Entidade que representa um nó na estrutura de um grafo de fluxo de controle. 
 * <br>
 * A construção dessa estrutura é feita através das classes {@link CFGBuilder} e {@link CFGProcessor}.
 * 
 * @author robson
 *
 */
public class CFGNode implements IElement {
	
	private List<InstructionHandle> instructions;
	private Map<CFGNode, CFGEdgeType> childNodes;
	private List<CFGNode> parentNode;
	private HashMap<Integer, CFGEdgeType> parentEdges;
	private SysMethod sysMethod;
	private CFGNode mergeNode;
	
	private Boolean tryStatement;
	private Boolean isReference;
	private boolean isFinallyNode;
	private boolean isOutTryNode;
	private boolean isOutSwitchNode;
	private boolean isSwitchNode;
	private boolean isCatchNode;
	private boolean isCaseNode;
	private boolean isEndNode;
	private boolean isOutRefNode;
	private boolean isOutIfNode;
	private boolean isIfNode;
	private boolean isIfTrueNode;
	private boolean isIfFalseNode;
	private boolean isLoopTrueNode;
	private boolean isLoopFalseNode;
	
	public CFGNode() {
		this.instructions = new ArrayList<InstructionHandle>();
		this.childNodes = new HashMap<CFGNode, CFGEdgeType>();
		this.parentEdges = new HashMap<Integer, CFGEdgeType>();
		this.parentNode = new LinkedList<CFGNode>();
		this.mergeNode = null;
		this.tryStatement = false;
		this.isOutTryNode = false;
		this.isReference = false;
		this.isCatchNode = false;
		this.isFinallyNode = false;
		this.isCaseNode = false;
		this.isOutSwitchNode = false;
		this.isSwitchNode = false;
		this.isIfNode = false;
		this.isIfFalseNode = false;
		this.isLoopFalseNode = false;
		this.isLoopTrueNode = false;
	}
	
	/**
	 * Adiciona uma instrução do tipo {@link InstructionHandle} na lista de todas
	 * as instruções processadas nesse nó.
	 * 
	 * @param instruction
	 * 		instrução a ser adicionada na lista
	 */
	public void addInstruction(InstructionHandle instruction) {
		this.instructions.add(instruction);
	}

	/**
	 * 
	 * @return todas as instruções pertencentes a este nó
	 */
	public List<InstructionHandle> getInstructions() {
		return instructions;
	}

	/**
	 * Adiciona um nó filho no grafo.
	 */
	public synchronized void addChildNode(CFGNode childNode, CFGEdgeType edgeType) {
		if(this.equals(childNode)){
			return;
		}
		
		if(childNode != null && childNode.getParents().isEmpty()) {
			this.childNodes.put(childNode, edgeType);
			if(this.childNodes.containsKey(childNode)){
				System.out.println("[CFGNode] Child added!");
			} else {
				System.err.println("[CFGNode] Child not added!");
			}
		}
		
		if(childNode.getChildElements().isEmpty()){
			childNode.setEndNode(true);
		} 
		
		childNode.setOwner(this);
		childNode.getParentEdges().put(this.hashCode(), edgeType);
		
		System.out.println("Aresta " + edgeType + " adicionada ao nó "  + this);
		this.setEndNode(false);
	}

	/**
	 * 
	 * @return o mapa de nós filhos
	 */
	public Map<CFGNode, CFGEdgeType> getChildNodes() {
		return childNodes;
	}
	
	public void setChildNodes(Map<CFGNode, CFGEdgeType> childNodes) {
		this.childNodes = childNodes;
		
		for(CFGNode childNode : this.childNodes.keySet()) {
			childNode.setOwner(this);
		}
	}

	/**
	 * 
	 * @return se o nó é pai de um block try/catch/finally
	 */
	public Boolean isTryStatement() {
		return tryStatement;
	}

	public void setTryStatement(Boolean tryStatement) {
		this.tryStatement = tryStatement;
	}

	/**
	 * 
	 * @return se o nó é uma referência a outro já processado
	 */
	public Boolean isReference() {
		return isReference;
	}

	public void setReference(Boolean isReference) {
		this.isReference = isReference;
	}

	public SysMethod getSysMethod() {
		return sysMethod;
	}

	public void setSysMethod(SysMethod sysMethod) {
		if(this.sysMethod == null) {
			this.sysMethod = sysMethod;
		}
	}

	@Override
	public IElement getOwner() {
		if(!parentNode.isEmpty())
			return parentNode.get(0);
		else
			return null;
	}

	public CFGNode getReturnNode() {
		return mergeNode;
	}

	public void setReturnNode(CFGNode returnNode) {
		this.mergeNode = returnNode;
	}

	public boolean isOutRefNode() {
		return isOutRefNode;
	}
	
	public HashMap<Integer, CFGEdgeType> getParentEdges() {
		return parentEdges;
	}

	public void setOutRefNode(boolean isOutRefNode) {
		this.isOutRefNode = isOutRefNode;
	}

	public boolean isFinallyNode() {
		return isFinallyNode;
	}

	public boolean isCaseNode() {
		return isCaseNode;
	}

	public void setCaseNode(boolean isCaseNode) {
		this.isCaseNode = isCaseNode;
	}

	public void setFinallyNode(boolean isFinallyNode) {
		this.isFinallyNode = isFinallyNode;
	}

	public boolean isCatchNode() {
		return isCatchNode;
	}
	
	public boolean isOutTryNode() {
		return isOutTryNode;
	}

	public void setOutTryNode(boolean isOutTryNode) {
		this.isOutTryNode = isOutTryNode;
	}

	public boolean isOutSwitchNode() {
		return isOutSwitchNode;
	}

	public void setOutSwitchNode(boolean isOutSwitchNode) {
		this.isOutSwitchNode = isOutSwitchNode;
	}

	public boolean isSwitchNode() {
		return isSwitchNode;
	}

	public void setSwitchNode(boolean isSwitchNode) {
		this.isSwitchNode = isSwitchNode;
	}

	public boolean isEndNode() {
		return isEndNode;
	}

	public void setEndNode(boolean isEndNode) {
		this.isEndNode = isEndNode;
	}

	public void setCatchNode(boolean isCatchNode) {
		this.isCatchNode = isCatchNode;
	}
	
	public IElement getOwner(int i) {
		if(parentNode.size() >= i){
			return parentNode.get(i);
		} else {
			return null;
		}
	}
	
	public List<CFGNode> getParents(){
		return parentNode;
	}

	public void setParents(List<CFGNode> parentNode) {
		if(parentNode == null){
			parentNode = new LinkedList<CFGNode>();
		}
		this.parentNode = parentNode;
	}

	@Override
	public void setOwner(IElement parentNode) {
		this.parentNode.add((CFGNode) parentNode);
	}
	
	public boolean isOutIfNode() {
		return isOutIfNode;
	}

	public void setOutIfNode(boolean isOutIfNode) {
		this.isOutIfNode = isOutIfNode;
	}

	public boolean isIfNode() {
		return isIfNode;
	}

	public void setIfNode(boolean isIfNode) {
		this.isIfNode = isIfNode;
	}

	public boolean isIfTrueNode() {
		return isIfTrueNode;
	}

	public void setIfTrueNode(boolean ifTrueNode) {
		this.isIfTrueNode = ifTrueNode;
	}

	public boolean isIfFalseNode() {
		return isIfFalseNode;
	}

	public void setIfFalseNode(boolean isIfFalseNode) {
		this.isIfFalseNode = isIfFalseNode;
	}

	public boolean isLoopTrueNode() {
		return isLoopTrueNode;
	}

	public void setLoopTrueNode(boolean isLoopTrueNode) {
		this.isLoopTrueNode = isLoopTrueNode;
	}

	public boolean isLoopFalseNode() {
		return isLoopFalseNode;
	}

	public void setLoopFalseNode(boolean isLoopFalseNode) {
		this.isLoopFalseNode = isLoopFalseNode;
	}
	
	@Override
	public String toString() {				
		return this.instructions.size() == 0 ? "[out]":
			this.tryStatement ? "[try] s:" + this.instructions.get(0).getPosition() + " e:" + this.instructions.get(instructions.size()-1).getPosition():
			new StringBuffer("[").append(this.instructions.get(0).getPosition()).append("]").toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instructions.isEmpty()) ? 0 : instructions.hashCode());
		result = prime * result
				+ ((tryStatement == null) ? 0 : tryStatement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CFGNode other = (CFGNode) obj;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (sysMethod == null) {
			if (other.sysMethod != null)
				return false;
		} else if (!sysMethod.equals(other.sysMethod))
			return false;
		if (tryStatement == null) {
			if (other.tryStatement != null)
				return false;
		} else if (!tryStatement.equals(other.tryStatement))
			return false;
		return true;
	}

	@Override
	public void addChild(IElement e) {
		System.err.println("Filho não adicionado");
	}

	@Override
	public Set<? extends IElement> getChildElements() {
		return this.getChildNodes().keySet();
	}
	
	public CFGEdgeType getChildTypeByNode(CFGNode childNode) {
		CFGEdgeType edge = this.childNodes.get(childNode);
		
		if(edge != null){
			return edge;
		}
		
		for (Map.Entry<CFGNode, CFGEdgeType> entry : this.childNodes.entrySet()){
		    if (entry.getKey().equals(childNode)) {
		    	edge = entry.getValue();         
		    }
		}
		
		if(edge != null){
			return edge;
		}
		
		System.err.println("[CFGNode.getChildTypeByNode] - " + childNode.toString() + 
				"\tAresta não encontrada");
		
		return null;
		
	}

	public boolean hasEnd() {
		if(this.instructions != null && this.getChildElements().isEmpty()) {
			
			for(InstructionHandle instructionHandle : this.getInstructions()) {
				
				if(instructionHandle.getInstruction() instanceof ReturnInstruction) {
					return true;
				}
			}
		}
		return false;
	}

	public void getRefToLeaves(CFGNode leaf, CFGEdgeType edgeType) {
		boolean isOutTryBlock = leaf.isTryStatement() &&
				leaf.getReturnNode() == null &&
				edgeType.equals(CFGEdgeType.OUT_TRY);
		
		boolean isEndNode = !leaf.equals(this) && leaf.isEndNode();
		
		boolean isFinallyNode = leaf.isFinallyNode() && !leaf.equals(this) && leaf.isEndNode();
		
		if(isOutTryBlock){
			boolean noChild = leaf.isEndNode() ||
					!leaf.isFinallyNode() ||
					leaf.getChildElements().isEmpty();
			Iterator<? extends IElement> iChild = leaf.getChildElements().iterator();
			while(iChild.hasNext()){
				CFGNode cfgNode = (CFGNode) iChild.next();
				noChild = !cfgNode.isCatchNode() ? false : noChild;
			}
			if(noChild){
				leaf.addChildNode(this, edgeType);
			}
		}
		
		if((isEndNode || isFinallyNode) && !isOutTryBlock){
			leaf.addChildNode(this, edgeType);
			System.out.println("[CFGNode] Referencia à folha " + leaf + "capturada para o nó " + this );
			
		} else if(leaf.equals(this)){
			
			return;
			
		} else {
			
			Iterator<? extends IElement> iChild = leaf.getChildElements().iterator();
			while(iChild.hasNext()){
				CFGNode cfgNode = (CFGNode) iChild.next();
				this.getRefToLeaves(cfgNode, edgeType);
			}			
		}
	}

	public void getRefToLoopRoot(CFGNode leaf, CFGEdgeType edgeType) {
		if(!containsCatchParent(leaf)){
			if(leaf.isEndNode()){
				leaf.addChildNode(this, edgeType);
			} else{
				for(IElement child : leaf.getChildElements()){
					CFGNode cfgNode = (CFGNode) child;
					this.getRefToLeaves(cfgNode, edgeType);
				}
			}
		} 		
	}

	private boolean containsCatchParent(CFGNode leaf) {
		if(!leaf.getParents().isEmpty()){
			for(CFGNode parent : leaf.getParents()){
				if(parent.isCatchNode()){
					return true;
				}
			}
			containsCatchParent(leaf.getParents().get(0));
		}
		return false;
	}

	
}
