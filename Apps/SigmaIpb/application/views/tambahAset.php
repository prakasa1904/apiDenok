
<form name="form1" id="form1" action="<?=base_url()?>index.php/pegawai/add" method="post">
	<?php
	if($this->session->flashdata('message')){
	echo "<i>".$this->session->flashdata('message')."</i>";
	}
 
	echo validation_errors();
	?>
	
<table width="400" border="1">
	<tr>
		<td>Kode Barang</td>
		<td><input name="kode" type="text" id="kode" value="<?php echo set_value('kode');?>"></td>
	</tr>
	<tr>
		<td>Nama</td>
		<td><input name="nama" type="text" id="nama" value="<?php echo set_value('nama');?>"></td>
	</tr>
	<tr>
		<td>Merk</td>
		<td><input name="merk" type="text" id="merk" value="<?php echo set_value('merk');?>"></td>
	</tr>
	<tr>
		<td>Tahun</td>
		<td><input name="tahun" type="text" id="tahun" value="<?php echo set_value('tahun');?>"></td>
	</tr>
	<tr>
		<td>Harga Satuan</td>
		<td><input name="harga" type="text" id="harga" value="<?php echo set_value('harga');?>"></td>
	</tr>
	<tr>
		<td>Sumber Dana</td>
		<td><input name="dana" type="text" id="dana" value="<?php echo set_value('dana');?>"></td>
	</tr>
	<tr>
		<td>Kondisi</td>
		<td><select name="kondisi" id="kondisi">
  			<option value="<?php echo set_value('baik');?>">Baik</option>
  			<option value="<?php echo set_value('rringan');?>">Rusak Ringan</option>
  			<option value="<?php echo set_value('rberat');?>">Rusak Berat</option>
		</select></td>
	</tr>
	<tr>
		<td>Lokasi</td>
		<td> <select name="aset" id="aset"><option value="0"> -- Ruangan -- </option>
		
        <?php
		foreach($aset->result() as $r)
		{?>
            <option value="<?php echo $r->kode_lokasi?>"><?php echo $r->nama_ruang?></option>
		<?php }
		?>		
		</td>
		
		<td><input name="tahun" type="text" id="tahun" value="<?php echo set_value('alamat');?>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input name="Add" type="submit" id="add" value="Submit"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><a href="<?php echo base_url()?>index.php/pegawai">Home>> </a></td>
	</tr>
</table>
</form>