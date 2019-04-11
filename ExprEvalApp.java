import java.lang.*; 
import java.util.*; 
import java.io.*; 
import org.antlr.v4.runtime.*; 
import org.antlr.v4.runtime.tree.*;
import java.util.Queue;
import java.util.LinkedList;
class EvalListener extends ExprBaseListener { 
// hash-map for variables' integer value for assignment 
	Map<String, Double> vars = new HashMap<String, Double>(); 
	Map<String, Integer> prior= new HashMap<String, Integer>();
// stack for expression tree evaluation 
	Stack<Double> numStack = new Stack<Double>();
	Stack<String> operatorStack = new Stack<String>();
	Queue<String> initialqueue  = new LinkedList<String>();
	int conditionflag = 0;
	int bracketflag = 0;
	Double token=-1.0;
	String stoken;

	public EvalListener() {
		prior.put("+", 0);
		prior.put("-", 0);
		prior.put("*", 1);
		prior.put("/", 1);
	}


	@Override 
	public void exitProg(ExprParser.ProgContext ctx) { 

	}

	@Override
	public void exitAssn(ExprParser.AssnContext ctx){	
	}

	@Override 
	public void exitExpr(ExprParser.ExprContext ctx) { 

	}
	@Override 
	public void enterExpr(ExprParser.ExprContext ctx) { 
		conditionflag=0;
	}

	@Override 
	public void visitTerminal(TerminalNode node) { 
		String s = node.getText();
		//System.out.println("Terminal: " + node.getText());
		if(conditionflag == 1){
			return;
		}
		if(conditionflag == 0){
			initialqueue.add(node.getText());
		}
	}
	@Override public void enterNum(ExprParser.NumContext ctx) { 
		if(conditionflag == 1){
			token = Double.parseDouble(ctx.getText());
			
		}
        	
	}
	@Override
    	public void enterAssn(ExprParser.AssnContext ctx) {
		conditionflag=1;
		stoken = ctx.getText();
    	}
	
}

public class ExprEvalApp {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        BufferedReader rd = new BufferedReader(new FileReader(file));
        String line = "";
        String tp ="1";
        while (true) {
            tp = rd.readLine();
            if(tp == null)
                break;
            line+=tp;
        }
        ExprLexer lexer = new ExprLexer(CharStreams.fromString(line));        
        // Get lexer
        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Pass tokens to parse
        ExprParser parser = new ExprParser(tokens);
        // Walk parse-tree and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        EvalListener listener = new EvalListener();
        // walk from the root of parse tree
        walker.walk(listener, parser.prog());
	
	
    }
}
