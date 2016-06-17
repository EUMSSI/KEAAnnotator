

/* First created by JCasGen Wed Sep 23 15:28:31 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Thu Jun 16 11:49:11 CEST 2016
 * XML source: D:/susanne/git/KEAAnnotator/src/main/resources/com/iai/uima/jcas/tcas/KeyPhraseTypeSystemDescriptor.xml
 * @generated */
public class KeyPhraseAnnotationReplaced extends KeyPhraseAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyPhraseAnnotationReplaced.class);
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
  protected KeyPhraseAnnotationReplaced() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KeyPhraseAnnotationReplaced(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KeyPhraseAnnotationReplaced(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public KeyPhraseAnnotationReplaced(JCas jcas, int begin, int end) {
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
  //* Feature: isAdjectiveReplacedWithNoun

  /** getter for isAdjectiveReplacedWithNoun - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsAdjectiveReplacedWithNoun() {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_isAdjectiveReplacedWithNoun == null)
      jcasType.jcas.throwFeatMissing("isAdjectiveReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_isAdjectiveReplacedWithNoun);}
    
  /** setter for isAdjectiveReplacedWithNoun - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsAdjectiveReplacedWithNoun(boolean v) {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_isAdjectiveReplacedWithNoun == null)
      jcasType.jcas.throwFeatMissing("isAdjectiveReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_isAdjectiveReplacedWithNoun, v);}    
   
    
  //*--------------*
  //* Feature: replacee

  /** getter for replacee - gets 
   * @generated
   * @return value of the feature 
   */
  public String getReplacee() {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_replacee == null)
      jcasType.jcas.throwFeatMissing("replacee", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return jcasType.ll_cas.ll_getStringValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_replacee);}
    
  /** setter for replacee - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setReplacee(String v) {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_replacee == null)
      jcasType.jcas.throwFeatMissing("replacee", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    jcasType.ll_cas.ll_setStringValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_replacee, v);}    
   
    
  //*--------------*
  //* Feature: isVerbReplacedWithNoun

  /** getter for isVerbReplacedWithNoun - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsVerbReplacedWithNoun() {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_isVerbReplacedWithNoun == null)
      jcasType.jcas.throwFeatMissing("isVerbReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_isVerbReplacedWithNoun);}
    
  /** setter for isVerbReplacedWithNoun - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsVerbReplacedWithNoun(boolean v) {
    if (KeyPhraseAnnotationReplaced_Type.featOkTst && ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeat_isVerbReplacedWithNoun == null)
      jcasType.jcas.throwFeatMissing("isVerbReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationReplaced_Type)jcasType).casFeatCode_isVerbReplacedWithNoun, v);}    
  }

    