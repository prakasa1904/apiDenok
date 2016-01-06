<div style="margin-top:20px;" id="tabel_view2">
	<table width="100%" border="1" align="center">
		<?php
			$total= $titik->num_rows;
			if($total == 0){
				echo " ";
			}
			else {
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
	<?php 
		} else { echo 'Tidak Ada Data'; }
	?>