package main.java.com.github.decyg.CrAgg

import com.decyg.CrAgg.CIFParser.CIFSingleton

/**
 * Created by declan on 01/02/2017.
 */


fun main(args : Array<String>){

    val test = """#------------------------------------------------------------------------------
#Date: 2016-02-19 16:29:56 +0200 (Fri, 19 Feb 2016) $
#Revision: 176759 $
#URL: svn://www.crystallography.net/cod/cif/2/00/68/2006882.cif $
#------------------------------------------------------------------------------
#
# This file is available in the Crystallography Open Database (COD),
# http://www.crystallography.net/. The original data for this entry
# were provided by IUCr Journals, http://journals.iucr.org/.
#
# The file may be used within the scientific community so long as
# proper attribution is given to the journal article from which the
# data were obtained.
#
data_2006882
loop_
_publ_author_name
'Angela Taylor'
'Edmund J. Eisenbraun'
'Clinton E. Browne'
'Elizabeth M. Holt'
_publ_section_title
;
 <i>cis</i>-<i>anti</i>-<i>cis</i>-Tricyclo[6.4.0.0^2,7^]dodecane-3,12-dione
 and
 <i>cis</i>-<i>anti</i>-<i>cis</i>-Tricyclo[6.4.0.0^2,7^]dodecane-3,9-dione
;
_journal_issue                   12
_journal_name_full               'Acta Crystallographica Section C'
_journal_page_first              1963
_journal_page_last               1966
_journal_paper_doi               10.1107/S0108270197011694
_journal_volume                  53
_journal_year                    1997
_chemical_formula_moiety         'C12 H16 O2'
_chemical_formula_sum            'C12 H16 O2'
_chemical_formula_weight         192.2
_chemical_name_systematic
;
cis-anti-cis-tricyclo[6.4.0.0^2.7^]dodecane-3,12-dione
;
_space_group_IT_number           61
_symmetry_cell_setting           orthorhombic
_symmetry_space_group_name_Hall  '-P 2ac 2ab'
_symmetry_space_group_name_H-M   'P b c a'
_cell_angle_alpha                90.0
_cell_angle_beta                 90.0
_cell_angle_gamma                90.0
_cell_formula_units_Z            8
_cell_length_a                   10.703(2)
_cell_length_b                   10.9900(10)
_cell_length_c                   17.584(5)
_cell_measurement_reflns_used    63
_cell_measurement_temperature    298
_cell_measurement_theta_max      12.433
_cell_measurement_theta_min      3.694
_cell_volume                     2068.3(7)
_computing_cell_refinement       XSCANS
_computing_data_collection       'XSCANS (Fait, 1991)'
_computing_data_reduction        XSCANS
_computing_molecular_graphics    'XP (Siemens, 1990)'
_computing_structure_refinement  'SHELXL97 (Sheldrick,1997)'
_computing_structure_solution    'SHELXS86 (Sheldrick, 1990)'
_diffrn_ambient_temperature      298
_diffrn_measurement_device_type  'Syntex P4 4-circle-diffractometer'
_diffrn_measurement_method       \q/2\q
_diffrn_radiation_monochromator  'highly oriented graphite crystal'
_diffrn_radiation_source         'fine-focus sealed tube'
_diffrn_radiation_type           'Mo K\a'
_diffrn_radiation_wavelength     0.71073
_diffrn_reflns_av_R_equivalents  0.0773
_diffrn_reflns_limit_h_max       13
_diffrn_reflns_limit_h_min       -1
_diffrn_reflns_limit_k_max       14
_diffrn_reflns_limit_k_min       -1
_diffrn_reflns_limit_l_max       22
_diffrn_reflns_limit_l_min       -1
_diffrn_reflns_number            2996
_diffrn_reflns_theta_full        27.49
_diffrn_reflns_theta_max         27.49
_diffrn_reflns_theta_min         2.32
_diffrn_standards_decay_%        0.01
_diffrn_standards_interval_count '97 reflections'
_diffrn_standards_number         3
_exptl_absorpt_coefficient_mu    0.082
_exptl_absorpt_correction_type   none
_exptl_crystal_colour            colorless
_exptl_crystal_density_diffrn    1.236
_exptl_crystal_description       rhomb
_exptl_crystal_F_000             832
_exptl_crystal_size_max          0.20
_exptl_crystal_size_mid          0.155
_exptl_crystal_size_min          0.15
_refine_diff_density_max         0.15
_refine_diff_density_min         -0.16
_refine_ls_extinction_coef       0.0013(3)
_refine_ls_extinction_method     SHELXL-97
_refine_ls_goodness_of_fit_ref   0.725
_refine_ls_hydrogen_treatment    noref
_refine_ls_matrix_type           full
_refine_ls_number_parameters     129
_refine_ls_number_reflns         2336
_refine_ls_R_factor_gt           0.0590
_refine_ls_shift/su_max          0.001
_refine_ls_shift/su_mean         0.000
_refine_ls_structure_factor_coef Fsqd
_refine_ls_weighting_details
'w=1/[\s^2^(Fo^2^)+(0.1363P)^2^] where P=(Fo^2^+2Fc^2^)/3'
_refine_ls_weighting_scheme      calc
_refine_ls_wR_factor_ref         0.1562
_reflns_number_gt                852
_reflns_number_total             2336
_reflns_threshold_expression     I>2\s(I)
_cod_data_source_file            sx1038.cif
_cod_data_source_block           1
_cod_depositor_comments
;
The following automatic conversions were performed:

'_refine_ls_weighting_scheme' value 'calc
w=1/[\s^2^(Fo^2^)+(0.1363P)^2^] where P=(Fo^2^+2Fc^2^)/3' was changed
to 'calc'. New tag '_refine_ls_weighting_details' was created. The
value of the new tag was set to 'w=1/[\s^2^(Fo^2^)+(0.1363P)^2^]
where P=(Fo^2^+2Fc^2^)/3'.

Automatic conversion script
Id: cif_fix_values 3143 2015-03-26 13:38:13Z robertas
;
_cod_original_sg_symbol_H-M      Pbca
_cod_database_code               2006882
loop_
_symmetry_equiv_pos_as_xyz
'x, y, z'
'-x+1/2, -y, z+1/2'
'-x, y+1/2, -z+1/2'
'x+1/2, -y+1/2, -z'
'-x, -y, -z'
'x-1/2, y, -z-1/2'
'x, -y-1/2, z-1/2'
'-x-1/2, y-1/2, z'
loop_
_atom_site_label
_atom_site_fract_x
_atom_site_fract_y
_atom_site_fract_z
_atom_site_U_iso_or_equiv
_atom_site_adp_type
_atom_site_calc_flag
_atom_site_refinement_flags
_atom_site_occupancy
_atom_site_disorder_assembly
_atom_site_disorder_group
_atom_site_type_symbol
O1 .0043(3) .8424(2) .17140(14) .1095(9) Uani d . 1 . . O
O2 -.0019(2) .64074(18) -.03676(13) .0868(7) Uani d . 1 . . O
C1 .2077(3) .5393(3) .22823(16) .0760(9) Uani d . 1 . . C
H1A .1532 .4778 .2483 .080 Uiso d R 1 . . H
H1B .2918 .5094 .2316 .080 Uiso d R 1 . . H
C2 .1915(3) .6515(3) .27677(18) .0844(10) Uani d . 1 . . C
H2A .2495 .7123 .2598 .080 Uiso d R 1 . . H
H2B .2084 .6335 .3292 .080 Uiso d R 1 . . H
C3 .0590(3) .7021(4) .26717(19) .0889(10) Uani d . 1 . . C
H3A .0433 .7677 .3019 .080 Uiso d R 1 . . H
H3B .0018 .6373 .2786 .080 Uiso d R 1 . . H
C4 .0387(3) .7409(3) .18756(18) .0750(9) Uani d . 1 . . C
C4' .0655(2) .6487(2) .12731(16) .0625(8) Uani d . 1 . . C
H4'A -.0082 .6064 .1108 .080 Uiso d R 1 . . H
C4" .1416(2) .6978(2) .05962(15) .0567(7) Uani d . 1 . . C
H4"A .1429 .7851 .0600 .080 Uiso d R 1 . . H
C5 .1054(3) .6482(2) -.01769(17) .0652(8) Uani d . 1 . . C
C6 .2112(3) .6084(3) -.06672(19) .0846(10) Uani d . 1 . . C
H6A .2613 .6779 -.0796 .080 Uiso d R 1 . . H
H6B .1804 .5730 -.1130 .080 Uiso d R 1 . . H
C7 .2937(3) .5190(3) -.02400(18) .0769(9) Uani d . 1 . . C
H7A .3567 .4856 -.0567 .080 Uiso d R 1 . . H
H7B .2418 .4537 -.0063 .080 Uiso d R 1 . . H
C8 .3538(2) .5798(3) .04378(17) .0712(9) Uani d . 1 . . C
H8A .4114 .6394 .0247 .080 Uiso d R 1 . . H
H8B .4009 .5214 .0725 .080 Uiso d R 1 . . H
C8' .2598(2) .6408(2) .09586(15) .0592(7) Uani d . 1 . . C
H8'A .3016 .6992 .1274 .080 Uiso d R 1 . . H
C8" .1744(2) .5608(2) .14535(15) .0598(7) Uani d . 1 . . C
H8"A .1629 .4843 .1198 .080 Uiso d R 1 . . H
loop_
_atom_site_aniso_label
_atom_site_aniso_U_11
_atom_site_aniso_U_22
_atom_site_aniso_U_33
_atom_site_aniso_U_12
_atom_site_aniso_U_13
_atom_site_aniso_U_23
O1 .119(2) .0999(18) .110(2) .0360(16) .0117(16) -.0170(15)
O2 .0861(15) .0827(15) .0914(16) .0096(13) -.0252(13) -.0057(11)
C1 .084(2) .0727(19) .071(2) -.0069(16) -.0011(18) .0185(16)
C2 .095(2) .091(2) .067(2) -.008(2) -.0081(18) .0051(18)
C3 .090(2) .102(2) .075(2) -.005(2) .015(2) -.0196(19)
C4 .0555(17) .083(2) .086(2) .0029(16) .0019(16) -.0088(18)
C4' .0535(14) .0627(15) .0713(17) -.0110(13) -.0004(14) -.0073(14)
C4" .0618(15) .0423(12) .0659(16) -.0018(12) -.0021(14) .0006(12)
C5 .0764(19) .0488(15) .0704(18) .0053(15) -.0102(17) .0085(13)
C6 .105(2) .0784(19) .071(2) .0117(19) -.001(2) -.0006(17)
C7 .0718(18) .0731(19) .086(2) .0106(16) .0106(18) -.0062(17)
C8 .0548(15) .0721(18) .087(2) -.0042(14) .0062(15) .0020(16)
C8' .0549(14) .0514(13) .0712(17) -.0103(12) .0012(14) -.0019(13)
C8" .0630(16) .0517(13) .0647(17) -.0101(13) -.0007(14) .0050(13)
loop_
_atom_type_symbol
_atom_type_scat_dispersion_real
_atom_type_scat_dispersion_imag
_atom_type_scat_source
C 0.0 0.0 'International Tables Vol C Tables 4.2.6.8 and 6.1.1.4'
H 0.0 0.0 'International Tables Vol C Tables 4.2.6.8 and 6.1.1.4'
O 0.0 0.0 'International Tables Vol C Tables 4.2.6.8 and 6.1.1.4'
loop_
_geom_angle_atom_site_label_1
_geom_angle_atom_site_label_2
_geom_angle_atom_site_label_3
_geom_angle
_geom_angle_publ_flag
C2 C1 C8" 112.9(2) yes
C1 C2 C3 109.9(3) yes
C4 C3 C2 110.2(3) yes
O1 C4 C3 122.2(3) yes
O1 C4 C4' 121.2(3) yes
C3 C4 C4' 116.5(3) yes
C4 C4' C4" 114.3(2) yes
C4 C4' C8" 115.0(2) yes
C4" C4' C8" 88.79(18) yes
C5 C4" C4' 115.6(2) yes
C5 C4" C8' 115.6(2) yes
C4' C4" C8' 88.40(18) yes
O2 C5 C6 123.1(3) yes
O2 C5 C4" 121.3(3) yes
C6 C5 C4" 115.5(3) yes
C5 C6 C7 110.2(3) yes
C8 C7 C6 110.6(2) yes
C8' C8 C7 112.9(2) yes
C8 C8' C8" 118.9(2) yes
C8 C8' C4" 118.2(2) yes
C8" C8' C4" 88.80(18) yes
C1 C8" C8' 119.5(2) yes
C1 C8" C4' 118.1(2) yes
C8' C8" C4' 88.54(18) yes
loop_
_geom_bond_atom_site_label_1
_geom_bond_atom_site_label_2
_geom_bond_site_symmetry_2
_geom_bond_distance
_geom_bond_publ_flag
O1 C4 . 1.209(3) yes
O2 C5 . 1.199(3) yes
C1 C2 . 1.509(4) yes
C1 C8" . 1.519(4) yes
C2 C3 . 1.532(5) yes
C3 C4 . 1.480(5) yes
C4 C4' . 1.494(4) yes
C4' C4" . 1.540(4) yes
C4' C8" . 1.547(4) yes
C4" C5 . 1.515(4) yes
C4" C8' . 1.548(4) yes
C5 C6 . 1.489(4) yes
C6 C7 . 1.519(4) yes
C7 C8 . 1.510(4) yes
C8 C8' . 1.517(4) yes
C8' C8" . 1.538(3) yes
"""

    println(CIFSingleton.RX_DataBlock)
    var res = CIFSingleton.RX_DataItems.find(test)
    while(res != null){

        println(res.range)
        res = res.next()
    }

}
