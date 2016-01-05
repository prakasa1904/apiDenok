<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.8.2.js"></script>
		<script type="text/javascript" src="<?php echo base_url();?>jquery/FusionCharts.js"></script>
		<script language="javascript" src="<?php echo base_url()?>jquery/xajax.js"></script>
		<script src="<?php echo base_url()?>openlayers/OpenLayers2.js" type="text/javascript"></script>
		<script src="<?php echo base_url()?>openlayers/build/ol.js" type="text/javascript"></script>
		<link type="text/css" href="<?php echo base_url()?>jquery/themes/base/ui.all.css" rel="stylesheet" />
		<link href="<?php echo base_url() . 'css/templatemo_style.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'css/ddsmoothmenu.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'css/style.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'openlayers/build/ol.css'; ?>" rel="stylesheet" type="text/css"/>
		<script src="http://maps.google.com/maps/api/js?v=3.5&amp;sensor=false"></script>
		<!-- Basic CSS definitions -->
		<style type="text/css">
			/* General settings */
			body{
				font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
				font-size: small;
			}
 
            /* The map and the location bar */
			#map{
				position: absolute;
				left: 0px;
				top: 243px;
				z-index: 0;
				background-color: #ffffff;
				width: 932px;
				height: 330px;
				border: 1px solid black;
			}
			
			#tabel_view{
				width: 910px;
				height: 400px;
				overflow: auto;
			}
			
			#tabel_view2{
				width: 893px;
				height: 22px;
				overflow: !important;
			}
			
			#menus{
				position: absolute;
				left: 0px;
				top: 203px;
				z-index: 1;
			}
			
			#notifikasi{background-color:red;font-size:8pt;position:relative;top:-4}
		</style>
		<script defer="defer" type="text/javascript">
			function init(){
				var bounds = new OpenLayers.Bounds(11880112, -731899.8125, 11881295, -731519.1875);
				
				var options = {
					minScale: 4261,
					controls: [],
					maxExtent: bounds,
					maxResolution: 397.5232391357422,
					projection: "EPSG:3857",
					units: 'm'
                };
				
                format = 'image/png';
				
				map = new OpenLayers.Map('map', options);
				
				var gmap = new OpenLayers.Layer.Google("Google Streets",
					{MAX_ZOOM_LEVEL: 24, MIN_ZOOM_LEVEL: 16}
				);
				
				// setup single tiled layer
				wing = new OpenLayers.Layer.WMS(
					"ipb_gis:level6 - Tiled", "http://localhost:8080/geoserver/ipb_gis/wms",
					{
						LAYERS: 'ipb_gis:level6',
						STYLES: '',
						transparent: "true",
						format: format,
					},
					{isBaseLayer: false}
				);
				
				// setup single tiled layer
				base = new OpenLayers.Layer.WMS(
					"ipb_gis:level6base - Untiled", "http://localhost:8080/geoserver/ipb_gis/wms",
					{
						LAYERS: 'ipb_gis:level6base',
						STYLES: '',
						transparent: "true",
						format: format,
					},
					{isBaseLayer: false}
				);
				
				map.addLayers([gmap, base]);
				
				// build up all controls
                map.addControl(new OpenLayers.Control.PanZoomBar()); //zoom
				
				map.addControl(new OpenLayers.Control.Navigation()); //pan map
				
				map.zoomToExtent(bounds);
				
				getMarker();
			}
			
			function getMarker(){
				// Define markers as "features" of the vector layer:
				var vectorLayer = new OpenLayers.Layer.Vector("Overlay");
				
				<?PHP echo $marker;?>
					
				map.addLayer(vectorLayer);
				
				//Add a selector control to the vectorLayer with popup functions
				var controls = {
					selector: new OpenLayers.Control.SelectFeature(vectorLayer, { onSelect: createPopup, onUnselect: destroyPopup })
				};
				
				function createPopup(feature) {
					feature.popup = new OpenLayers.Popup("pop",
						feature.geometry.getBounds().getCenterLonLat(),
						null,
						'<div class="markerContent">'+
							'<div class="foto" style="float:left">'+
								'<img src="'+feature.attributes.foto+'" height=100px>'+
							'</div>'+
							'<div class="data">'
								+feature.attributes.nama_barang+'<br/>'
								+feature.attributes.ruang+'<br/>'+
							'</div>'+
						'</div>',
						null,
						true,
						function(){controls['selector'].unselectAll();}
					);
					
					feature.popup.panMapIfOutOfView = true;
					feature.popup.autoSize = true;
					map.addPopup(feature.popup);
				}
				
				function destroyPopup(feature){
					feature.popup.destroy();
					feature.popup = null;
				}
				
				map.addControl(controls['selector']);
				controls['selector'].activate();
			}
			
			function mergeNewParams(params){
				tiled.mergeNewParams(params);
				wing.mergeNewParams(params);
			}
			
			function filter(){
				map.addLayers([wing, tiled]);
				var h;
				var filter = "";
				var arg = arguments.length-1;
				
				for(h=0;h<arguments.length;h++){
					if(arguments[h]!="S" && arguments[h]!="T"){
						filter += "wing = "+arguments[h];
						if(h!=arg){
							filter += " or ";
						}
					}
				}
				
				// by default, reset all filters
				var filterParams = {
					filter: null,
					cql_filter: null,
					featureId: null
				};
				
				filterParams["cql_filter"] = filter;
				
				// merge the new filter definitions
				mergeNewParams(filterParams);
			}
			
			function filter_wing(){
				var katakunci = document.getElementById('katakunci').value;
				var berdasarkan = document.getElementById('berdasarkan').value;
				var namawingnya = [];
				var j = 1;
				var wing;
				
				$.ajax({
					type: 'POST',
					url: '<?php echo base_url()?>index.php/map/marker6',
					data: {katakunci:katakunci, berdasarkan:berdasarkan},
					success: function(data){
						wing = document.getElementById('jumWing').value;
						var i;
						for(i=0;i<wing;i++){
							namawingnya[i] = document.getElementById('namawing'+j).value;
							j++;
						}
						filter.apply(this, namawingnya);
					}
				});
			}
		</script>
		<title>SIGMA IPB</title>
	</head>
	<body onload="init()">
		<div id="wrapper">
			<div id="header">
				<img src="<?php echo base_url(); ?>images/banner sigma.jpg"/>
				<div id="menus">
					<div id="nav">
						<ul class="nav">
							<li ><a href="<?php echo base_url().'map/level6'?>">Pencarian Aset</a></li>
							<li><a href="#">Manajemen Aset</a>
								<ul>
									<li><a href="<?php echo base_url().'map/manajemen'?>">tambah</a></li>
									<li><a href="<?php echo base_url().'map/edit'?>">ubah & hapus</a></li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
				<div id="map"></div>
				<div id="nav-top">
					<ul>
						<a href="<?php echo base_url().'map/index'?>">
							<li id="tab-map">
								<div>level 1</div>
							</li>
						</a>
						<a href="<?php echo base_url().'map/level2'?>">
							<li id="tab-map">
								<div>level 2</div>
							</li>
						</a>
						<a href="<?php echo base_url().'map/level3'?>">
							<li id="tab-map">
								<div>level 3</div>
							</li>
						</a>
						<?php
						if (isset($_SESSION['jum4'])&&($_SESSION['jum4']>0)){?>
						<a href="<?php echo base_url().'map/level4'?>">
							<li id="tab-map" class="new">
								<div>level 4</div>
							</li>
						</a>
						<?php } else {?>
						<a href="<?php echo base_url().'map/level4'?>">
							<li id="tab-map">
								<div>level 4</div>
							</li>
						</a>
						<?php } ?>
						<?php
						if (isset($_SESSION['jum5'])&&($_SESSION['jum5']>0)){?>
						<a href="<?php echo base_url().'map/level5'?>">
							<li id="tab-map" class="new">
								<div>level 5</div>
							</li>
						</a>
						<?php } else {?>
						<a  href="<?php echo base_url().'map/level5'?>">
							<li id="tab-map">
								<div>level 5</div>
							</li>
						</a>
						<?php } ?>
						<a style="color:#fdfdfd;" href="<?php echo base_url().'map/level6'?>">
							<li id="tab-map"  class="sel">
								<div>level 6</div>
							</li>
						</a>
					</ul>
				</div>
			</div>
			<div id="content">
				<div class="left-col">
					<div id="welcome">
						<br />
						<?php include 'include/wing.php'; ?>
						<br />
						<br />
						<table width="" border="0" cellpadding="7" align="center">
							<?php
								if(!isset($_SESSION['katakunci'])){
								//jika session belum di set/register
							?>
							<form method="post" action='<?PHP echo site_url("map/marker6")?>'>
								<tr>
									<td width="">
										<?php
											echo '<label>Kata Kunci : </label><select name="berdasarkan">';
											$attributes = array('class' => 'form-inline reset-margin', 'id' => 'myform');
											//save the columns names in a array that we will use as filter         
											$options_aset = array();
											foreach ($aset as $array){
												foreach ($array as $key => $value){
													$options_aset[$key] = $key;
													echo '<option value='.$key.'>'.$key.'</option>';
												}
												break;
											}
											echo "</select>";
										?>
									</td>
									<td width="">
										<label>Kata Kunci : </label><input type="text" name="katakunci" autofocus required oninvalid="this.setCustomValidity('Kata kunci harus diisi!')"/>
									</td>
									<td>
										<input type="submit" value="Cari"/>
									</td>
								</tr>
							</form>
							<?php } else { ?>
							<tr>
								<td>Hasil pencarian <?php echo $_SESSION['katakunci']; ?> di Level 6</td>
								<td><a href="<?php echo base_url('map/reset_cari6'); ?>">Cari Lagi</a></td>
							</tr>
							<tr>
								<td><input type="hidden" name="berdasarkan" id="berdasarkan" value="<?php echo $_SESSION['berdasarkan']; ?>"></td>
								<td><input type="hidden" name="katakunci" id="katakunci" value="<?php echo $_SESSION['katakunci'];  ?>" ></td>
								<?php echo '<script type="text/javascript">', 'filter_wing();', '</script>';?>
							</tr>
							<?php } ?>
						</table>
						<span id="tabel">
							<?php if(isset($titik)){?>
							<div style="margin-top:20px;" id="tabel_view2">
								<table width="100%" border="1" align="center">
									<?php
										$total= $titik->num_rows;
										if($total == 0){
											echo " ";
										} else {
									?>
									Total : <?php echo $total ?> <br>
									<tr>
										<th width="24">No</th>
										<th width="40">Wing</th>
										<th width="250">Nama Barang</th>
										<th width="150">Merk</th>
										<th width="74">Tahun</th>
										<th>Nama Ruang</th>
									</tr>
									<?php } ?>
								</table>
							</div>
							<?php
								if($titik->num_rows() != 0){ //$titik->num_rows() counting jumlah data titik berupa array
							?>
							<input type="hidden" id="jumWing" value="<?php echo $wing->num_rows();?>">
							<?php
								$nama = "namawing";
								$ulang = 1;
								foreach($wing->result() as $w){
							?>
							<input type="hidden" id="<?php echo $nama.$ulang?>" value="<?php echo $w->wing?>">
							<?php
								$ulang++;}
							?>
							<div style="margin-top:20px;" id="tabel_view">
								<table width="100%" border="1" align="center">
									<?php $i=1; foreach($titik->result() as $r){ //$titik->result() hasil array dari pengolahan query data titik lalu dilooping
										if ($r->wing == 1){$color = "#df382a";}
										elseif ( $r->wing ==2){$color = "#942697";}
										elseif ( $r->wing ==3){$color = "#4fe0e8";}
										elseif ( $r->wing ==4){$color = "#f79656";}
										elseif ( $r->wing ==5){$color = "#ebef43";}
										elseif ( $r->wing ==6){$color = "#f780a0";}
										elseif ( $r->wing ==7){$color = "#600000";}
										elseif ( $r->wing ==8){$color = "#2b880f";}
										elseif ( $r->wing ==9){$color = "#8f68bf";}
										elseif ( $r->wing ==11){$color = "#ff1e1e";}
										elseif ( $r->wing ==12){$color = "#faff24";}
										elseif ( $r->wing ==17){$color = "#9ff80c";}
										elseif ( $r->wing ==18){$color = "#311aff";}
										elseif ( $r->wing ==19){$color = "#d91aa5";}
										elseif ( $r->wing ==20){$color = "#786f1d";}
										elseif ( $r->wing ==22){$color = "#9c9c9c";}
										else{$color = "#ffffff";}
									?>
									<tr>
										<td width="24"><?php echo $i++;?></td>
										<td style="background-color: <?php echo $color; ?>" width="15"></td><td width="18"><?php echo $r->wing;?></td>
										<td width="250"><?php echo $r->nama_barang;?></td>
										<td width="150"><?php echo $r->merk_type;?></td>
										<td width="74"><?php echo $r->tahun;?></td>
										<td><?php echo $r->nama_ruang;?></td>
									</tr>
									<?php } ?>
								</table>
							</div>
							<?php } else { echo 'Tidak Ada Data'; }} ?>
						</span>
					</div>
				</div>
				<div class="clear" id="footc"></div>
				<div id="footer">
					&copy; Copyright Alfin fauzano 2015. All rights reserved | designed by <a href="http://www.freewebsitetemplates.com">free website templates</a>
				</div>
			</div>
		</div>
	</body>
</html>