

/* First created by JCasGen Wed Oct 19 18:06:05 CEST 2016 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Oct 19 18:06:05 CEST 2016
 * XML source: /home/jgrivolla/GitHub/EUMSSI-UIMA/KEAAnnotator/src/main/resources/com/iai/uima/jcas/tcas/KeyPhraseTypeSystemDescriptor.xml
 * @generated */
public class posSequenceCount extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(posSequenceCount.class);
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
  protected posSequenceCount() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public posSequenceCount(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public posSequenceCount(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public posSequenceCount(JCas jcas, int begin, int end) {
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
  //* Feature: posSequenceCount

  /** getter for posSequenceCount - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPosSequenceCount() {
    if (posSequenceCount_Type.featOkTst && ((posSequenceCount_Type)jcasType).casFeat_posSequenceCount == null)
      jcasType.jcas.throwFeatMissing("posSequenceCount", "com.iai.uima.jcas.tcas.posSequenceCount");
    return jcasType.ll_cas.ll_getStringValue(addr, ((posSequenceCount_Type)jcasType).casFeatCode_posSequenceCount);}
    
  /** setter for posSequenceCount - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPosSequenceCount(String v) {
    if (posSequenceCount_Type.featOkTst && ((posSequenceCount_Type)jcasType).casFeat_posSequenceCount == null)
      jcasType.jcas.throwFeatMissing("posSequenceCount", "com.iai.uima.jcas.tcas.posSequenceCount");
    jcasType.ll_cas.ll_setStringValue(addr, ((posSequenceCount_Type)jcasType).casFeatCode_posSequenceCount, v);}    
  }

    