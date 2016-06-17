
/* First created by JCasGen Wed Sep 23 15:40:43 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** 
 * Updated by JCasGen Thu Jun 16 11:49:11 CEST 2016
 * @generated */
public class KeyPhraseAnnotationEnriched_Type extends KeyPhraseAnnotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyPhraseAnnotationEnriched_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyPhraseAnnotationEnriched_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyPhraseAnnotationEnriched(addr, KeyPhraseAnnotationEnriched_Type.this);
  			   KeyPhraseAnnotationEnriched_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyPhraseAnnotationEnriched(addr, KeyPhraseAnnotationEnriched_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyPhraseAnnotationEnriched.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
 
  /** @generated */
  final Feature casFeat_enrichedWithLongForm;
  /** @generated */
  final int     casFeatCode_enrichedWithLongForm;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getEnrichedWithLongForm(int addr) {
        if (featOkTst && casFeat_enrichedWithLongForm == null)
      jcas.throwFeatMissing("enrichedWithLongForm", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_enrichedWithLongForm);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnrichedWithLongForm(int addr, boolean v) {
        if (featOkTst && casFeat_enrichedWithLongForm == null)
      jcas.throwFeatMissing("enrichedWithLongForm", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_enrichedWithLongForm, v);}
    
  
 
  /** @generated */
  final Feature casFeat_enrichedWithAbbreviation;
  /** @generated */
  final int     casFeatCode_enrichedWithAbbreviation;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getEnrichedWithAbbreviation(int addr) {
        if (featOkTst && casFeat_enrichedWithAbbreviation == null)
      jcas.throwFeatMissing("enrichedWithAbbreviation", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_enrichedWithAbbreviation);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnrichedWithAbbreviation(int addr, boolean v) {
        if (featOkTst && casFeat_enrichedWithAbbreviation == null)
      jcas.throwFeatMissing("enrichedWithAbbreviation", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_enrichedWithAbbreviation, v);}
    
  
 
  /** @generated */
  final Feature casFeat_enrichedWithCountry;
  /** @generated */
  final int     casFeatCode_enrichedWithCountry;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getEnrichedWithCountry(int addr) {
        if (featOkTst && casFeat_enrichedWithCountry == null)
      jcas.throwFeatMissing("enrichedWithCountry", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_enrichedWithCountry);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnrichedWithCountry(int addr, boolean v) {
        if (featOkTst && casFeat_enrichedWithCountry == null)
      jcas.throwFeatMissing("enrichedWithCountry", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_enrichedWithCountry, v);}
    
  
 
  /** @generated */
  final Feature casFeat_enrichedWithRegion;
  /** @generated */
  final int     casFeatCode_enrichedWithRegion;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getEnrichedWithRegion(int addr) {
        if (featOkTst && casFeat_enrichedWithRegion == null)
      jcas.throwFeatMissing("enrichedWithRegion", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_enrichedWithRegion);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnrichedWithRegion(int addr, boolean v) {
        if (featOkTst && casFeat_enrichedWithRegion == null)
      jcas.throwFeatMissing("enrichedWithRegion", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_enrichedWithRegion, v);}
    
  
 
  /** @generated */
  final Feature casFeat_enrichment;
  /** @generated */
  final int     casFeatCode_enrichment;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getEnrichment(int addr) {
        if (featOkTst && casFeat_enrichment == null)
      jcas.throwFeatMissing("enrichment", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    return ll_cas.ll_getStringValue(addr, casFeatCode_enrichment);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnrichment(int addr, String v) {
        if (featOkTst && casFeat_enrichment == null)
      jcas.throwFeatMissing("enrichment", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched");
    ll_cas.ll_setStringValue(addr, casFeatCode_enrichment, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public KeyPhraseAnnotationEnriched_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_enrichedWithLongForm = jcas.getRequiredFeatureDE(casType, "enrichedWithLongForm", "uima.cas.Boolean", featOkTst);
    casFeatCode_enrichedWithLongForm  = (null == casFeat_enrichedWithLongForm) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enrichedWithLongForm).getCode();

 
    casFeat_enrichedWithAbbreviation = jcas.getRequiredFeatureDE(casType, "enrichedWithAbbreviation", "uima.cas.Boolean", featOkTst);
    casFeatCode_enrichedWithAbbreviation  = (null == casFeat_enrichedWithAbbreviation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enrichedWithAbbreviation).getCode();

 
    casFeat_enrichedWithCountry = jcas.getRequiredFeatureDE(casType, "enrichedWithCountry", "uima.cas.Boolean", featOkTst);
    casFeatCode_enrichedWithCountry  = (null == casFeat_enrichedWithCountry) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enrichedWithCountry).getCode();

 
    casFeat_enrichedWithRegion = jcas.getRequiredFeatureDE(casType, "enrichedWithRegion", "uima.cas.Boolean", featOkTst);
    casFeatCode_enrichedWithRegion  = (null == casFeat_enrichedWithRegion) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enrichedWithRegion).getCode();

 
    casFeat_enrichment = jcas.getRequiredFeatureDE(casType, "enrichment", "uima.cas.String", featOkTst);
    casFeatCode_enrichment  = (null == casFeat_enrichment) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enrichment).getCode();

  }
}



    