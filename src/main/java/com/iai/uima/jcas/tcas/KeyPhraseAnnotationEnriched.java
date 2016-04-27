

/* First created by JCasGen Wed Sep 23 15:40:43 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Mon Apr 25 14:01:52 CEST 2016
 * XML source: D:/susanne/git/KEAAnnotator/src/main/resources/com/iai/uima/jcas/tcas/KeyPhraseTypeSystemDescriptor.xml
 * @generated */
public class KeyPhraseAnnotationEnriched extends KeyPhraseAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyPhraseAnnotationEnriched.class);
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
  protected KeyPhraseAnnotationEnriched() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KeyPhraseAnnotationEnriched(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KeyPhraseAnnotationEnriched(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public KeyPhraseAnnotationEnriched(JCas jcas, int begin, int end) {
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
  //* Feature: enrichedWithLongForm

  /** getter for enrichedWithLongForm - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEnrichedWithLongForm() {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithLongForm == null)
      jcasType.jcas.throwFeatMissing("enrichedWithLongForm", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithLongForm);}
    
  /** setter for enrichedWithLongForm - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnrichedWithLongForm(boolean v) {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithLongForm == null)
      jcasType.jcas.throwFeatMissing("enrichedWithLongForm", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithLongForm, v);}    
   
    
  //*--------------*
  //* Feature: enrichedWithAbbreviation

  /** getter for enrichedWithAbbreviation - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEnrichedWithAbbreviation() {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithAbbreviation == null)
      jcasType.jcas.throwFeatMissing("enrichedWithAbbreviation", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithAbbreviation);}
    
  /** setter for enrichedWithAbbreviation - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnrichedWithAbbreviation(boolean v) {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithAbbreviation == null)
      jcasType.jcas.throwFeatMissing("enrichedWithAbbreviation", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithAbbreviation, v);}    
   
    
  //*--------------*
  //* Feature: enrichedWithCountry

  /** getter for enrichedWithCountry - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEnrichedWithCountry() {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithCountry == null)
      jcasType.jcas.throwFeatMissing("enrichedWithCountry", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithCountry);}
    
  /** setter for enrichedWithCountry - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnrichedWithCountry(boolean v) {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithCountry == null)
      jcasType.jcas.throwFeatMissing("enrichedWithCountry", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithCountry, v);}    
   
    
  //*--------------*
  //* Feature: enrichedWithRegion

  /** getter for enrichedWithRegion - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getEnrichedWithRegion() {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithRegion == null)
      jcasType.jcas.throwFeatMissing("enrichedWithRegion", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithRegion);}
    
  /** setter for enrichedWithRegion - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnrichedWithRegion(boolean v) {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichedWithRegion == null)
      jcasType.jcas.throwFeatMissing("enrichedWithRegion", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichedWithRegion, v);}    
   
    
  //*--------------*
  //* Feature: enrichment

  /** getter for enrichment - gets 
   * @generated
   * @return value of the feature 
   */
  public String getEnrichment() {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichment == null)
      jcasType.jcas.throwFeatMissing("enrichment", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return jcasType.ll_cas.ll_getStringValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichment);}
    
  /** setter for enrichment - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnrichment(String v) {
    if (KeyPhraseAnnotationEnriched_Type.featOkTst && ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeat_enrichment == null)
      jcasType.jcas.throwFeatMissing("enrichment", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    jcasType.ll_cas.ll_setStringValue(addr, ((KeyPhraseAnnotationEnriched_Type)jcasType).casFeatCode_enrichment, v);}    
  }

    