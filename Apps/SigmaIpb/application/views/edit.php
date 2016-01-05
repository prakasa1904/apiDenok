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
			body{
				font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
				font-size: small;
			}
			#isi{
				background-color: #ffffff;
				width: 932px;
				height: 570px;
				border: 1px solid black;
			}
			#tabel_viewEdt{
				width:910px;
				height: 400px;
				overflow:auto;
			}
			#tabel_view2Edt{
				width:893px;
				height:22px;
				overflow: !important;
			}
		</style>
		<script defer="defer" type="text/javascript">
			function cari_query(){
				if (document.getElementById('katakunci').value == 0){
					alert(" Masukkan Kata Kunci Pencarian");
				} else {
					var katakunci = document.getElementById('katakunci').value;
					var berdasarkan = document.getElementById('berdasarkan').value;
					var namawingnya = [];
					var j = 1;
					var wing;
					
					$.ajax({
						type: 'POST',
						url: '<?php echo base_url()?>index.php/map/pencarianhEdt',
						data: {katakunci:katakunci, berdasarkan:berdasarkan},
						success: function(data){
							$("#tabel2").html(data);
							//get_markers();
						}
					});
					
					$.ajax({
						type: 'POST',
						url: '<?php echo base_url()?>index.php/map/pencarianEdt',
						data: {katakunci:katakunci, berdasarkan:berdasarkan},
						success: function(data){
							$("#tabel").html(data);
							//get_markers() ; 	
							//----------------------------------------------------		
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
			}
		</script>
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
					<h3>Ubah dan Hapus Aset</h3>
					<div class="left-col">
						<div id="welcome">
							<br />
							<table width="" border="0" cellpadding="7" align="center">
								<tr>
									<td width="">
										<?php
											$attributes = array('class' => 'form-inline reset-margin', 'id' => 'myform');
											//save the columns names in a array that we will use as filter
											$options_aset = array();
											foreach ($aset as $array){
												foreach ($array as $key => $value){
													$options_aset[$key] = $key;
												}
												break;
											}
											$js = 'id="berdasarkan" class="span2"';
											echo form_label('Kategori:', 'order');
											echo form_dropdown('order', $options_aset, $order, $js);
										?>
									</td>
									<td  width="">
										<label>Kata Kunci : </label><input type="text" id="katakunci">
									</td>
									<td>
										<input type="button" value="Cari" Onclick="cari_query();" />
									</td>
								</tr>
							</table>
							<div style="margin-top:20px;" id="tabel_view2Edt">
								<span id="tabel2"></span>
							</div>
							<div style="margin-top:20px;" id="tabel_viewEdt">
								<span id="tabel"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="clear" id="footc"></div>
			<div id="footer">
				Copyright statement goes here. All rights reserved | designed by <a href="http://www.freewebsitetemplates.com">free website templates</a>
			</div>
		</div>
	</body>
</html>
