<table width="100%" border="1" align="center">
<?php $total= $titik->num_rows;?>

<?php if ($total == 0)
{echo" ";} 
else { ?> 
Total : <?php echo $total ?> <br>
<tr><th width="40">Mark</th><th width="24">No</th><th width="250">Nama Barang</th><th width="150">Merk</th><th width="74">Tahun</th><th>Nama Ruang</th></tr>
</table>
<?php }?>