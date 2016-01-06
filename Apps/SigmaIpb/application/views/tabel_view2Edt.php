<div style="margin-top:20px;" id="tabel_view2Edt">
	<table width="100%" border="1" align="center">
		<?php 
			$total = $titik->num_rows;
			if ($total == 0){
				echo " ";
			} else { ?> 
			Total : <?php echo $total ?> <br>
			<tr>
				<th width="24" align="center">No</th>
				<th width="250" align="center">Nama Barang</th>
				<th width="150" align="center">Merk</th>
				<th width="74"align="center">Tahun</th>
				<th align="center">Nama Ruang</th>
				<th width="120" align="center">Aksi</th>
			</tr>
		<?php } ?>
	</table>
<div>