<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<?php echo base_url()?>jquery/jquery-1.8.2.js"></script>
		<script type="text/javascript" src="<?php echo base_url();?>jquery/FusionCharts.js"></script>
		<script language="javascript" src="<?php echo base_url()?>jquery/xajax.js"></script>
		<script src="<?php echo base_url()?>openlayers/OpenLayers2.js" type="text/javascript"></script>
		<script src="<?php echo base_url()?>openlayers/build/ol.js" type="text/javascript"> </script>	
		<link type="text/css" href="<?php echo base_url()?>jquery/themes/base/ui.all.css" rel="stylesheet" />
		<link href="<?php echo base_url() . 'css/templatemo_style.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'css/ddsmoothmenu.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'css/style.css'; ?>" rel="stylesheet" type="text/css" />
		<link href="<?php echo base_url() . 'openlayers/build/ol.css'; ?>" rel="stylesheet" type="text/css"/>
		<!-- Basic CSS definitions --> 
		<style type="text/css">
			/* General settings */
			body{
				font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
				font-size: small;
			}
			/* The map and the location bar */
			#isi{
				background-color: #ffffff;
				width: 932px;
				height: 830px;
				border: 1px solid black;
			}
			#tabel_view{
				width:700px;
				height: 400px;
				overflow:auto;
			}
			#tabel_view2{
				width:683px;
				height:22px;
				overflow: !important;
			}
		</style>
		<title>SIGMA IPB</title>
	</head>
	<body>
		<div id="wrapper">
			<div id="header">
				<img src="<?php echo base_url(); ?>images/banner sigma.jpg"/>
				<div id="nav">
					<ul class="nav">
						<li ><a href="<?php echo base_url().'map/level5'?>">Pencarian Aset</a></li>
						<li>
							<a href="#">Manajemen Aset</a>
							<ul>
								<li><a href="<?php echo base_url().'map/manajemen'?>">tambah</a></li>
								<li><a href="<?php echo base_url().'map/edit'?>">ubah & hapus</a></li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<div id="isi">
				<div id="content">
					<br />
					<h3>Ubah Data Aset</h3>
					<div class="left-col">
						<div id="welcome">
							<br />
							<form name="form1" id="form1" action="<?=base_url()?>index.php/map/editas" method="post" enctype="multipart/form-data">
								<?php
									if($this->session->flashdata('message')){
										echo "<i>".$this->session->flashdata('message')."</i>";
									}
									echo validation_errors();
								?>
								<table width="600" border="0">
									<input name="ida" type="hidden" id="ida" value="<?php echo set_value('ida',isset($aset['id_barang'])?$aset['id_barang'] :'');?>">
									<tr>
										<td>Kode Barang</td>
										<td><input size="70" name="kode" type="text" id="kode" value="<?php echo set_value('kode',isset($aset['kode_barang'])?$aset['kode_barang'] :'');?>"></td>
									</tr>
									<tr>
										<td>Nama</td>
										<td><input size="70" name="nama" type="text" id="nama" value="<?php echo set_value('nama',isset($aset['nama_barang'])?$aset['nama_barang'] :'');?>"></td>
									</tr>
									<tr>
										<td>Merk</td>
										<td><input size="70" name="merk" type="text" id="merk" value="<?php echo set_value('merk',isset($aset['merk_type'])?$aset['merk_type'] :'');?>"></td>
									</tr>
									<tr>
										<td>Tahun</td>
										<td><input size="70" name="tahun" type="text" id="tahun" value="<?php echo set_value('tahun',isset($aset['tahun'])?$aset['tahun'] :'');?>"></td>
									</tr>
									<tr>
										<td>Harga Satuan</td>
										<td><input size="70" name="harga" type="text" id="harga" value="<?php echo set_value('harga',isset($aset['harga_satuan'])?$aset['harga_satuan'] :'');?>"></td>
									</tr>
									<tr>
										<td>Sumber Dana</td>
										<td><input size="70" name="dana" type="text" id="dana" value="<?php echo set_value('dana',isset($aset['sumber_dana'])?$aset['sumber_dana'] :'');?>"></td>
									<tr>
										<td>Ruang</td>
										<td>
											<select name="ruang" id="ruang">
												<?php
													$query = "SELECT * FROM tbl_lokasi";
													$result = pg_query($query);
													while($row = pg_fetch_row($result)){
														if($row[0]==$aset['kode_lokasi']){
															echo "<option value='".$row[0]."' selected>".$row[1]."</option>";
														} else {
															echo "<option value='".$row[0]."'>".$row[1]."</option>";
														}	
													}
												?>
											</select>
										</td>
									</tr>
									<tr>
										<td>Foto</td>
										<?php 
											if($aset['foto'] == NULL){
												echo "<td><input type=file name=upload id=upload/></td>";
											} else {
												echo "<td><input type=file name=upload id=upload/></td>";
												echo "<tr><td>&nbsp;</td><td><img src=$aset[foto] height=150px></td></tr>";
											}
										?>
										
									</tr>
									<tr>
										<td>Longitude</td>
										<?php
											if($aset['koordinat']){
												$koordinat = $aset['koordinat'];
												$koordinat = str_replace('(','',$koordinat);
												$koordinat = str_replace(')','',$koordinat);
												$pisah = explode(",",$koordinat);
												$long = $pisah[0];
												$lat = $pisah[1];
											} else {
												$long = '';
												$lat = '';
											}
										?>
										<td><input size="70" name="longitude" type="text" id="longitude" value="<?php echo set_value('longitude',isset($long)?$long :'');?>" readonly></td>
										<td><center><img src="../../../images/refresh.png" style="cursor:pointer" height="20" width="20" onclick="update_koordinat()" title="Perbaharui koordinat."/></center></td>
									</tr>
									<tr>
										<td>Latitude</td>
										<td><input size="70" name="latitude" type="text" id="latitude" value="<?php echo set_value('latitude',isset($lat)?$lat :'');?>" readonly></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td><input name="update" type="submit" id="update" value="Simpan"></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td><a href="<?php echo base_url()?>map/edit">Home>> </a></td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="clear" id="footc"></div>
			<div id="footer">
				Copyright statement goes here. All rights reserved | designed by <a href="http://www.freewebsitetemplates.com">free website templates</a>
				<script type="text/javascript">
					function update_koordinat(){
						var view = new ol.View();
						
						var geolocation = new ol.Geolocation({
							tracking: true,
							projection: view.getProjection()
						});
						
						// update the HTML page when the position changes.
						geolocation.on('change', function() {
							var lonlat = new OpenLayers.LonLat(geolocation.getPosition()).transform('EPSG:3857', 'EPSG:4326').toString();
							
							var pisah = lonlat.split(',');
							var x = pisah[0].split('=');
							var y = pisah[1].split('=');
							
							document.getElementById('longitude').value = x[1];
							document.getElementById('latitude').value = y[1];
						});
						
						// handle geolocation error
						/*
						geolocation.on('error', function(error){
							alert("Anda tidak mengizinkan geolocation.");
						});*/
					}
				</script>
			</div>
		</div>
	</body>
</html>