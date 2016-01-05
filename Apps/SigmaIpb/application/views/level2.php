<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	 <script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.4.2.min.js"></script>
     <script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="<?php echo base_url();?>jquery/FusionCharts.js"></script>
	<script language="javascript" src="<?php echo base_url()?>jquery/xajax.js"></script>
	<script src="<?php echo base_url()?>openlayers/OpenLayers2.js" type="text/javascript"> </script> 
    <link type="text/css" href="<?php echo base_url()?>jquery/themes/base/ui.all.css" rel="stylesheet" />
	<link href="<?php echo base_url() . 'css/templatemo_style.css'; ?>" rel="stylesheet" type="text/css" />
	<link href="<?php echo base_url() . 'css/ddsmoothmenu.css'; ?>" rel="stylesheet" type="text/css" />
	<link href="<?php echo base_url() . 'css/style.css'; ?>" rel="stylesheet" type="text/css" />
	
     	<!-- Basic CSS definitions --> 
        <style type="text/css"> 
            /* General settings */
            body {
                font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
                font-size: small;
				font-color: blue;
            }
 
            /* The map and the location bar */
            #map {
				background-color: #ffffff;
                width: 932px;
                height: 330px;
                border: 1px solid black;
			}
			
			#tabel_view{
				width:700px;
				height: 400px;
				overflow:auto;
			}
        </style>
		
		
	 <script defer="defer" type="text/javascript"> 
			var vectorLayer = new OpenLayers.Layer.Vector("Overlay");	//buat makers
			$(document).ready(function () {
					$('#tabel_lokasi td img.delete_row').live('click', 
						function(){ 
							$(this).parent().parent().remove();
					});
					
				});
			
			$(function() {
				$('#datepicker').datepicker({		//buat date picker
				    dateFormat  : "dd-mm-yy", 
					changeMonth: true,		//ganti bulan
					yearRange: "2002:2012",
					changeYear: true		//ganti tahun
				});
				
				$('#datepicker2').datepicker({
				    dateFormat  : "dd-mm-yy", 
					changeMonth: true,
					yearRange: "2002:2012",
					changeYear: true
				});
			});
			
			function get_kab(id)
			{
				if(id == 0)
				{
					$("#v_kab").html('<input type="hidden" name="kab" id="kab" value="0" />');
				}
				else
				{
					 $.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/get_kabupaten',
						  data:{id:id},
						  success: function(data){
								$("#v_kab").html(data);	
						  }
						}); 
				}
			}
			
			function get_kab3(id,baris)
			{
				if(id == 0)
				{
					$("#v_kab").html('<input type="hidden" name="kab" id="kab" value="0" />');
				}
				else
				{
					 $.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/get_kabupaten3',
						  data:{id:id},
						  success: function(data){
								$("#v_kab_"+baris).html(data);	
						  }
						}); 
				}
			}
			
			function get_kab2(id)
			{
				if(id == 0)
				{
					$("#v_kab").html('<input type="hidden" name="kab" id="kab" value="0" />');
				}
				else
				{
					 $.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/get_kabupaten2',
						  data:{id:id},
						  success: function(data){
								$("#v_kab").html(data);	
						  }
						}); 
				}
			}
			
			
			function ubah_interface()
			{
				if(time.checked == 1 && loc.checked == 1 )
				{
					$.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/pilih_lokasi1',
						  success: function(data){
								$("#tabel_lokasi").html(data);	
						  }
						}); 
				}
				else if (time.checked == 0 && loc.checked == 1 )
				{
					$.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/pilih_lokasi2',
						  success: function(data){
								$("#tabel_lokasi").html(data);	
						  }
						}); 
				}
				else if (time.checked == 0 && loc.checked == 0 )
				{ 
				  $("#tabel_lokasi").html('<input type="hidden" name="prov" id="prov" value="0" /><input type="hidden" name="kab" id="kab" value="0" /> <input type="hidden" name="ket_lokasi" id="ket_lokasi" value="0"/>');
				}
				else if (time.checked == 1 && loc.checked == 0 )
				{
					$("#tabel_lokasi").html('<input type="hidden" name="prov" id="prov" value="0" /><input type="hidden" name="kab" id="kab" value="0" /> <input type="hidden" name="ket_lokasi" id="ket_lokasi" value="0"/>');
				}
				
			}
			
			function get_lokasi(nilai)
			{
				if(nilai==0)
				{
					$("#span_lokasi").html("");
				}
				else if(nilai==1)
				{
					$.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/get_provinsi',
						  success: function(data){
								$("#span_lokasi").html(data);	
						  }
						}); 
				}
				else if (nilai==2)
				{
					$.ajax({
						  type: 'POST',
						  url: '<?php echo base_url()?>index.php/map/get_provinsi2',
						  success: function(data){
								$("#span_lokasi").html(data);	
						  }
						}); 
				}
			}
			
			function input_prov(jum){
			
				var prov=document.getElementById('prov').value;
				if($('#total_row').val() < 19) {
					var total = $('#total_row').val();
					var jumlah = jum;
					var total2 = parseInt(total)+parseInt(jum);
					
					
					$.ajax({
						url: '<?php echo base_url()?>index.php/map/tambah_lokasi',
						data: {prov:prov},
						type: 'POST',
						success: function(data){
							$('#baris_'+total2).html(data);
							$('#total_row').val(total2);
							
						}
					});
				}	
				
			}
			
			function input_kabupaten(jum){
			
				var prov=document.getElementById('prov').value;
				var kab=document.getElementById('kab').value;
				if($('#total_row').val() < 19) {
					var total = $('#total_row').val();
					var jumlah = jum;
					var total2 = parseInt(total)+parseInt(jum);
					$.ajax({
						url: '<?php echo base_url()?>index.php/map/tambah_lokasi2',
						data: {prov:prov,kab:kab,baris:total},
						type: 'POST',
						success: function(data){
							$('#baris_'+total2).html(data);
							$('#total_row').val(total2);
							
						}
					});
				}	
				
			}
			
			
			function cari_query()
			{
				
				var tgl1=document.getElementById('datepicker').value;	//NGAMBIL NILAI POST DARI INPUT TYPE YG BERID
				var tgl2=document.getElementById('datepicker2').value;
				var prov=document.getElementById('prov').value;
				var kab=document.getElementById('kab').value;
				var ket_lokasi=$("input[type='radio'][name='ket_lokasi']:checked").val();
				
				
				var prov_list = new Array();
					  $("select[name='prov_x']").each(function(i){
						prov_list[i] = $(this).val();
					  });
					  
					  
				var list_kab = new Array();
					  $("select[name='list_kab']").each(function(i){
						list_kab[i] = $(this).val();
					  });  

				if(time.checked == 1){var waktu=1;}else{ var waktu=0;}
				if(loc.checked == 1){var lokasi=1;}else{ var lokasi=0;}
				
			    $.ajax({
							type: 'POST',
							url: '<?php echo base_url()?>index.php/map/tabel_proc',
				data: {tgl1:tgl1, tgl2:tgl2,prov:prov,kab:kab,waktu:waktu,lokasi:lokasi,ket_lokasi:ket_lokasi,prov_list:prov_list,list_kab:list_kab},
							success: function(data){
							$("#tabel").html(data);  
							get_markers();			
							}
						});	
			}
			
			
            function init(){
                // if this is just a coverage or a group of them, disable a few items,
                // and default to jpeg format
                format = 'image/png';
				$("#count").val(0);
				$("#datepicker").val("");
				$("#datepicker2").val("");
				$("#prov").val("");
				$("#kab").val("");
				
              var bounds = new OpenLayers.Bounds(
                    11880113, -731908.1875,
                    11881066, -731519.125
                );
                
                var options = {
					minScale: 4261,
					
                    controls: [],
                    maxExtent: bounds,
                    maxResolution: 397.5232391357422,
					 numZoomLevels: 4,
                    projection: "EPSG:3857",
                    units: 'm'
                };
                map = new OpenLayers.Map('map', options);
            
                // setup tiled layer
                tiled = new OpenLayers.Layer.WMS(
                );
            
                // setup single tiled layer
                untiled = new OpenLayers.Layer.WMS(
                   "ipb_gis:level2 - Tiled", "http://localhost:8080/geoserver/ipb_gis/wms",
                    {
                        LAYERS: 'ipb_gis:level2',
                        STYLES: '',
                        format: format,
                    }
                );
        
                map.addLayers([untiled, tiled]);

                // build up all controls
                map.addControl(new OpenLayers.Control.PanZoomBar({	//zoom
                   //position: new OpenLayers.Pixel(2, 15)
                }));
                map.addControl(new OpenLayers.Control.Navigation());		//pan map
                map.zoomToExtent(bounds);
                
                // support GetFeatureInfo
                map.events.register('click', map, function (e) {
                    var params = {
                        REQUEST: "GetFeatureInfo",
                        srs: map.layers[0].params.SRS};
                });
            }
			
			//fungsi untuk memetakan titik panas 
			function get_markers()
			{
				// baca data
				var lat = new Array();
					  $("input[name='lat']").each(function(i){
						lat[i] = $(this).val();
					  });
					  
				var long = new Array();
					  $("input[name='long']").each(function(i){
						long[i] = $(this).val();
					  });  
					  
				 //mapping marker
				 epsg4326 =  new OpenLayers.Projection("EPSG:4326"); //WGS 1984 projection
   				 projectTo = map.getProjectionObject(); //The map projection (Spherical Mercator)
				
					 for (var i = 0; i < lat.length; i++) {
					 var feature = new OpenLayers.Feature.Vector(
							new OpenLayers.Geometry.Point( long[i], lat[i] ).transform(epsg4326, projectTo),
							{description:'This is the value of<br>the description attribute'} ,
							{externalGraphic: 'http://localhost:8080/geoserver/openlayers/img/marker.png', graphicHeight: 20, graphicWidth: 16, graphicXOffset:-12, graphicYOffset:-25  }
							);
							vectorLayer.addFeatures(feature);
					 }
					    
							map.addLayer(vectorLayer);
			}
			
		function reset_map()
		{
		    vectorLayer.removeAllFeatures();
			vectorLayer.refresh({force:true});
			time.checked=0;
			loc.checked=0;
			$("#tabel").html("");
			$("#tabel_lokasi").html("");
			$("#grafik").html("");
			$("#datepicker").val("");
			$("#datepicker2").val("");

			$("#tabel_lokasi").html('<input type="hidden" name="prov" id="prov" value="0" /><input type="hidden" name="kab" id="kab" value="0" /><input type="hidden" name="ket_lokasi" id="ket_lokasi" value="0"/>');
			
		}
		
		function open_grafik()
		{
		  		var tgl1=document.getElementById('datepicker').value;
				var tgl2=document.getElementById('datepicker2').value;
				
				var prov=document.getElementById('prov').value;
				var kab=document.getElementById('kab').value;	
				
				if(time.checked == 1){var waktu=1;}else{ var waktu=0;}
				if(loc.checked == 1){var lokasi=1;}else{ var lokasi=0;}	
			
		  if(lokasi==0 && waktu==1)
		  {
			   $.ajax({
							type: 'POST',
		url: '<?php echo base_url()?>index.php/map/grafik/'+tgl1+'/'+tgl2+'/'+lokasi+'/'+waktu+'/'+prov+'/'+kab,
							success: function(data){
							$("#grafik").html(data);  
										
							}
						});	
		
		  }
		  else if(lokasi==1 && waktu==0)
		  {
			
				
				 $.ajax({
							type: 'POST',
							url: '<?php echo base_url()?>index.php/map/grafik2/'+prov+'/'+kab,
							success: function(data){
							$("#grafik").html(data);  
										
							}
						});	
				
		  }
		  else if(lokasi==1 && waktu==1)
		  {
			    var ket_lokasi=$("input[type='radio'][name='ket_lokasi']:checked").val();
				if(ket_lokasi==1)
				{
					var prov_list = new Array();
						  $("select[name='prov_x']").each(function(i){
							prov_list[i] = $(this).val();
						  });
					var prov=document.getElementById('prov').value;	  
						  
					$.ajax({
						url: '<?php echo base_url()?>index.php/map/grafik4/'+tgl1+'/'+tgl2,
						data: {prov_list:prov_list,prov:prov},
						type: 'POST',
						success: function(data){
							$("#grafik").html(data)
							
						}
					});
					//window.some_id = 5;
					//var w=window.open('index.php/map/grafik4/'+tgl1+'/'+tgl2,'_blank');
					//w.myVariable = prov_list;
				
					
				
				}

				else if(ket_lokasi==2)
				{
					var list_kab = new Array();
					  $("select[name='list_kab']").each(function(i){
						list_kab[i] = $(this).val();
					  }); 
					  
					  $.ajax({
						url: '<?php echo base_url()?>index.php/map/grafik5/'+tgl1+'/'+tgl2,
						data: {list_kab:list_kab,kab:kab},
						type: 'POST',
						success: function(data){
							$("#grafik").html(data)
							
						}
					});
				}
		  }
		  
		  
		}
        </script>
		
		
    <title>SIGMA IPB</title>
    </head>
    <body onload="init()">
	
<div id="wrapper">
  <div id="header">
    <img src="<?php echo base_url(); ?>images/banner sigma.jpg"/>
    
    <div id="nav">
      <ul>
        <li class="home"><a href="#">Pencarian Aset</a></li>
        <li><a href="#">Manajemen Aset</a></li>
      </ul>
    </div>
	<div id="map"> 
        </div>
	<div id="nav-top">
      <ul>
	    <a href="<?php echo base_url().'map/index'?>">
        <li id="tab-map" >
          <div>level 1</div>
        </li></a>
		<a style="color:#fdfdfd;" href="<?php echo base_url().'map/level2'?>">
        <li id="tab-map" class="sel">
          <div>level 2</div>
        </li></a>
		<a href="<?php echo base_url().'map/level3'?>">
        <li id="tab-map">
          <div>level 3</div>
        </li></a>
		<a  href="<?php echo base_url().'map/level4'?>">
        <li id="tab-map" >
          <div>level 4</div>
        </li></a>
		<a href="<?php echo base_url().'map/level5'?>">
        <li id="tab-map">
          <div>level 5</div>
        </li></a>
		<a href="<?php echo base_url().'map/level6'?>">
        <li id="tab-map">
          <div>level 6</div>
        </li></a>
      </ul>
    </div>
	
  </div>
  <div id="content">
  	
    <div class="left-col">
      <div id="welcome">
       <!--<form id="searchbox" action="">
	   <input id="search" type="text" placeholder="Cari Aset">
       <input id="submit" type="submit" value="Cari">
       </form>-->
      <h3>Kriteria Pencarian</h3>
        
        <fieldset style="border: groove" >
		<table width="300" border="0">
		<tr>
		<td colspan="3">
        <input type="checkbox" name="time" id="time" onclick="ubah_interface();"/>Berdasarkan waktu<br /></td>
        </tr>
      
        <tr>
        <p><td><br />Tanggal Awal </td> <td><br /> : </td><td><br /><input type="text" id="datepicker"></td></p></tr>
        <tr>
        <p><td>Tanggal Akhir<br /><br /></td> <td> :<br /><br /></td><td><input type="text" id="datepicker2"><br /><br /></td></p></tr></table></fieldset>
        <fieldset style="border: groove">
        <table width="400" border="0">
        <tr>
        <td colspan="3">
       <br /> <input type="checkbox" name="loc" id="loc" onclick="ubah_interface();"/>Berdasarkan lokasi <br /><br /></td></tr>
       
      	</table>
        <span id="tabel_lokasi">
        <input type="hidden" name="prov" id="prov" value="0"/>
         <input type="hidden" name="kab" id="kab" value="0"/>
         <input type="hidden" name="ket_lokasi" id="ket_lokasi" value="0"/>
        </span>
        </fieldset>
        <br />
        <input type="button" name="btn-1" value="Cari" Onclick="cari_query();"/>
        <input type="button" name="btn-2" value="Reset Map"  Onclick="reset_map();"/>
        <input type="hidden" name="count" id="count" value="0" />
       
        
        <div style="margin-top:20px;" id="tabel_view">
        <span id="tabel">
        </span>
        
        
		</div>
        
        <div style="margin-top:20px;" width="100%">
        <span id="grafik">
        </span>
        
        
		</div>
        </div>    

  </div>
  <div class="clear" id="footc"> </div>
  <div id="footer"> Copyright statement goes here. All rights reserved | designed by <a href="http://www.freewebsitetemplates.com">free website templates</a></div>
</div>
</div>


   

    </body>
</html>
