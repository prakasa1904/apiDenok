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