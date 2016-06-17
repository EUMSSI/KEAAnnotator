

/* First created by JCasGen Wed Sep 23 15:28:31 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Thu Jun 16 11:49:11 CEST 2016
 * XML source: D:/susanne/git/KEAAnnotator/src/main/resources/com/iai/uima/jcas/tcas/KeyPhraseTypeSystemDescriptor.xml
 * @generated */
public class KeyPhraseAnnotationDeprecated extends KeyPhraseAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyPhraseAnnotationDeprecated.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected KeyPhraseAnnotationDeprecated() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KeyPhraseAnnotationDeprecated(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KeyPhraseAnnotationDeprecated(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public KeyPhraseAnnotationDeprecated(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: containsStopword

  /** getter for containsStopword - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getContainsStopword() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsStopword == null)
      jcasType.jcas.throwFeatMissing("containsStopword", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsStopword);}
    
  /** setter for containsStopword - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContainsStopword(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsStopword == null)
      jcasType.jcas.throwFeatMissing("containsStopword", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsStopword, v);}    
   
    
  //*--------------*
  //* Feature: endsWithAdjective

  /** getter for endsWithAdjective - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEndsWithAdjective() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_endsWithAdjective == null)
      jcasType.jcas.throwFeatMissing("endsWithAdjective", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_endsWithAdjective);}
    
  /** setter for endsWithAdjective - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEndsWithAdjective(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_endsWithAdjective == null)
      jcasType.jcas.throwFeatMissing("endsWithAdjective", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_endsWithAdjective, v);}    
   
    
  //*--------------*
  //* Feature: containsAdverb

  /** getter for containsAdverb - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getContainsAdverb() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsAdverb == null)
      jcasType.jcas.throwFeatMissing("containsAdverb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsAdverb);}
    
  /** setter for containsAdverb - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContainsAdverb(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsAdverb == null)
      jcasType.jcas.throwFeatMissing("containsAdverb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsAdverb, v);}    
   
    
  //*--------------*
  //* Feature: containsFiniteVerb

  /** getter for containsFiniteVerb - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getContainsFiniteVerb() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsFiniteVerb == null)
      jcasType.jcas.throwFeatMissing("containsFiniteVerb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsFiniteVerb);}
    
  /** setter for containsFiniteVerb - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContainsFiniteVerb(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsFiniteVerb == null)
      jcasType.jcas.throwFeatMissing("containsFiniteVerb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsFiniteVerb, v);}    
   
    
  //*--------------*
  //* Feature: isContainedInLongerKeyPhrase

  /** getter for isContainedInLongerKeyPhrase - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsContainedInLongerKeyPhrase() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_isContainedInLongerKeyPhrase == null)
      jcasType.jcas.throwFeatMissing("isContainedInLongerKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_isContainedInLongerKeyPhrase);}
    
  /** setter for isContainedInLongerKeyPhrase - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsContainedInLongerKeyPhrase(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_isContainedInLongerKeyPhrase == null)
      jcasType.jcas.throwFeatMissing("isContainedInLongerKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_isContainedInLongerKeyPhrase, v);}    
   
    
  //*--------------*
  //* Feature: containsIncompleteNer

  /** getter for containsIncompleteNer - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getContainsIncompleteNer() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsIncompleteNer == null)
      jcasType.jcas.throwFeatMissing("containsIncompleteNer", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsIncompleteNer);}
    
  /** setter for containsIncompleteNer - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContainsIncompleteNer(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_containsIncompleteNer == null)
      jcasType.jcas.throwFeatMissing("containsIncompleteNer", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_containsIncompleteNer, v);}    
   
    
  //*--------------*
  //* Feature: equalsNer

  /** getter for equalsNer - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEqualsNer() {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_equalsNer == null)
      jcasType.jcas.throwFeatMissing("equalsNer", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_equalsNer);}
    
  /** setter for equalsNer - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEqualsNer(boolean v) {
    if (KeyPhraseAnnotationDeprecated_Type.featOkTst && ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeat_equalsNer == null)
      jcasType.jcas.throwFeatMissing("equalsNer", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationDeprecated_Type)jcasType).casFeatCode_equalsNer, v);}    
  }

    