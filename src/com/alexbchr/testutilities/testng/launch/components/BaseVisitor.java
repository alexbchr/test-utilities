package com.alexbchr.testutilities.testng.launch.components;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import com.alexbchr.testutilities.testng.ui.util.Utils;
import com.alexbchr.testutilities.testng.util.signature.ASTMethodDescriptor;
import com.alexbchr.testutilities.testng.util.signature.IMethodDescriptor;
import com.alexbchr.testutilities.testng.util.signature.MethodDescriptor;

import com.google.common.collect.Sets;

public class BaseVisitor extends ASTVisitor implements ITestContent {
  // List<MethodDeclaration>
  private Set<IMethodDescriptor> m_testMethods = Sets.newHashSet();
  private Set<IMethodDescriptor> m_factoryMethods = Sets.newHashSet();
  private Set<String> m_groups = Sets.newHashSet();
  protected boolean m_typeIsTest;
  protected String m_annotationType;
  
  public BaseVisitor(boolean f) {
    super(f);
  }
  
  public BaseVisitor() {
    super();
  }
  
  public boolean isTestNGClass() {
    return m_testMethods.size() > 0 || m_factoryMethods.size() > 0; 
  }
  
  public String getAnnotationType() {
    if(null != m_annotationType) {
      return m_annotationType;
    }
    
    return m_testMethods.iterator().next().getAnnotationType();
  }
  
  public Set<IMethodDescriptor> getTestMethods() {
    return m_testMethods;
  }
  
  public boolean hasTestMethods() {
    return m_typeIsTest || m_testMethods.size() > 0 || m_factoryMethods.size() > 0;
  }
    
  public Collection<String> getGroups() {
    return m_groups;
  }

  protected void addGroup(String groupNames) {
    groupNames = Utils.stripDoubleQuotes(groupNames);
    final String[] names = Utils.split(groupNames, ",");
    for(int i = 0; i < names.length; i++) {
//      ppp("    FOUND GROUP:" + names[i]);
      m_groups.add(names[i]);
    }
  }
  
  protected void addTestMethod(MethodDeclaration md, String annotationType) {
    if(md.isConstructor()) {
      return; // constructors cannot be marked as test methods
    }
    IMethodDescriptor imd = new ASTMethodDescriptor(md, annotationType);
    m_testMethods.add(imd);
  }
  
  protected void addFactoryMethod(MethodDeclaration md, String annotationType) {
    IMethodDescriptor imd = new ASTMethodDescriptor(md, annotationType);
    m_factoryMethods.add(imd);
  }

  public static void ppp(String s) {
    System.out.println("[BaseVisitor] " + s);
  }

  public boolean isTestMethod(IMethod imethod) {
    if(m_typeIsTest) {
      return true;
    }

    return m_testMethods.contains(new MethodDescriptor(imethod));
  }

}
