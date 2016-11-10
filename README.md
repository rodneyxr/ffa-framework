# File Flow Analysis (FFA)
This project is a build script analysis tool that uses file flow analysis and abstract interpretation to approximate possible abstract 
file structures of build scripts. We can then use these results to ensure successful execution, code transplantation and identify 
dependency issues.

## About the Project
As of now, I am working on a build script analysis framework that will allow a user to scan for potential problems in build scripts
prior to a build. We do this by using techniques like file flow analysis and abstract interpretation to approximate possible abstract
file structures resulting from build scripts. We can use the results from these techniques to ensure successful execution, code
transplantation and identify dependency issues. On top of this we may eventually be able to write plugins to scan source code for
patterns. For example Android has a privacy policy and an application may be limited to where it can send user information. We may be
able to run a string based analysis on the Android app and detect a privacy policy violation for accessing a network it is not allowed
to.

## How It Works
We have made a universal grammar that all languages can be reduced to. So we need to get a script such as an ant, maven, gradle, or
makefile and convert it to our grammar. Next we parse this grammar using a tool called ANTLR4 which yields an abstract syntax tree (AST)
and I then convert to a control flow graph (CFG). With the CFG, we have the entire flow of control of the build script and we have also
implemented the visitor pattern. From here we just traverse the graph and perform our analysis. Since we have our visitor pattern,
developers may write plugins and create their own analysis if they wish.

## Resources
[File Flow Analysis Javadoc](http://rodneyxr.github.io/fileflowanalysis/) - Javadoc for this project.  
[Java SE 8 API](https://docs.oracle.com/javase/8/docs/api/) - The API specification for the Java™ Platform, Standard Edition.  
[JUnit 4](http://junit.org/junit4/) - JUnit is a simple framework to write repeatable tests.  

## Tools
[ANTLR v4](http://www.antlr.org/) - ANother Tool for Language Recognition is a powerful parser generator  
[Graphviz](http://www.graphviz.org/) - Open source graph visualization software  

## Related Repositories
[File Flow Grammar](https://github.com/rodneyxr/file-flow-grammar) - The universal grammar used in this project.  
[Analysis](https://github.com/rodneyxr/analysis) - Collection of analyses that use this framework.  
[BRICS Automaton](https://github.com/rodneyxr/brics-automaton) - Finite-state automaton library.  
