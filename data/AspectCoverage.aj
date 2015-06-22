import org.aspectj.lang.Signature;
import annotations.MethodDeclaration;
import annotations.TestCaseDeclaration;
import dao.Repository;
import java.util.logging.*;

/**
 * This class is a aspect class responsible by coverage trace.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0 
 *
 */
public aspect AspectCoverage {
	private Repository repository;
	private Logger logger = Logger.getLogger("trace");
	pointcut traceMethods() : execution(@MethodDeclaration * *(..)) && !within(AspectCoverage);
	pointcut traceJUnitTests () : execution(@TestCaseDeclaration * *(..)) && !within(AspectCoverage);
	pointcut traceStatements () : get(* *.watchPriorJApp);
	before () : traceJUnitTests() {
		Signature sig = thisJoinPointStaticPart.getSignature();
		repository = new Repository();
        String pkgName = sig.getDeclaringType().getPackage().getName();
        String suiteName = thisJoinPoint.getTarget().getClass().getSimpleName();
        String tcName = sig.getName();
        repository.addTestCase(pkgName, suiteName, tcName);
		logger.logp(Level.INFO, sig.getDeclaringType().getName(), sig.getName(), "Test Case");
	}
	after(): traceJUnitTests(){
		if(repository != null) repository.commit();
	}
	before () : traceMethods(){
		Signature sig = thisJoinPointStaticPart.getSignature();
		Class pck = sig.getDeclaringType();
		String pckName = "";
		if (pck.getPackage() != null) pckName = sig.getDeclaringType().getPackage().getName();
		String classeName = sig.getDeclaringType().getName();
		classeName = classeName.replace(pckName+".", "");	
		String name = sig.toString();
		int index =  name.lastIndexOf(".")+1;
		name = name.substring(index, name.length());
		if(repository != null) repository.addMethod(pckName, classeName, name);
		logger.logp(Level.INFO, sig.getDeclaringType().getName(), sig.getName(), "Method");
	}
	before () : traceStatements(){
		int lineNumber = thisJoinPointStaticPart.getSourceLocation().getLine() + 1;
		if(repository != null)repository.addStatement(lineNumber + "");
		logger.logp(Level.INFO, "", String.valueOf(lineNumber), "Line");
	}
}

