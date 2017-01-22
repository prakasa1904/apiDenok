<?php
	class Query_model extends CI_Model{
		function Query_model(){
			parent::__construct();
		}
		
		public function get_aset_by_id($id){
			$this->db->select('*');
			$this->db->from('tbl_aset');
			$this->db->where('id_barang', $id);
			$query = $this->db->get();
			return $query->result_array();
		}
		
		/**
		* Fetch petugas data from the database
		* possibility to mix search, filter and order
		* @param string $search_string 
		* @param strong $order
		* @param string $order_type 
		* @param int $limit_start
		* @param int $limit_end
		* @return array
		*/
		
		function getAset($id){
			$data = array();
			$this->db->where('id_barang',$id);
			$this->db->limit(1);
			$query = $this->db->get('tbl_aset');
			if($query->num_rows() > 0){
				$data = $query->row_array();
			}
			return $data;
		}
		
		public function get_aset($search_string=null, $order=null, $order_type='Asc', $limit_start=null, $limit_end=null){
			$this->db->select('*');
			$this->db->from('tbl_aset');
			if($search_string){
				$this->db->like('LOWER(nama_barang)', strtolower($search_string));
			}
			$this->db->group_by('id_barang');
			if($order){
				$this->db->order_by($order, $order_type);
			}
			else{
				$this->db->order_by('id_barang', $order_type);
			}
			if($limit_start && $limit_end){
				$this->db->limit($limit_start, $limit_end);
			}
			if($limit_start != null){
				$this->db->limit($limit_start, $limit_end);
			}
			$query = $this->db->get();
			return $query->result_array();
		}
		
		function get_cari4($katakunci, $berdasarkan){
			$sql = "select a.*, l.* from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '4' AND a.kode_lokasi = l.kode_lokasi";
			return $this->db->query($sql);
		}
		
		function get_cari($katakunci, $berdasarkan){
			$sql = "select a.*, l.* from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '5' AND a.kode_lokasi = l.kode_lokasi";
			return $this->db->query($sql);
		}
		
		function get_cari6($katakunci, $berdasarkan){
			$sql="select a.*, l.* from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '6' AND a.kode_lokasi = l.kode_lokasi";
			return $this->db->query($sql);
		}
		
		function get_cariEdit($katakunci, $berdasarkan){
			$sql = "select a.*, l.* from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND a.kode_lokasi = l.kode_lokasi";
			return $this->db->query($sql);
		}
		
		//fungsi untuk menambah data aset ke dalam tabel dari form isian
		function addAset($d){
			if($d['koordinat'] != NULL && $d['foto'] != NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'koordinat' => $d['koordinat'],
					'foto' => $d['foto']
				);
			} elseif($d['koordinat'] == NULL AND $d['foto'] != NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'foto' => $d['foto']
				);
			} elseif($d['koordinat'] != NULL AND $d['foto'] == NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'koordinat' => $d['koordinat']
				);
			} else{
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi']
				);
			}
			return $this->db->insert('tbl_aset',$data);
		}
		
		//fungsi untuk mengedit data pegawai berdasarkan id
		function editAset($d){
			/*
			
			$koordinat = $this->input->post('longitude').','.$this->input->post('latitude');
			if($this->input->post('longitude')){
				$data = array(
							'kode_barang' => $this->input->post('kode'),
							'nama_barang' => $this->input->post('nama'),
							'merk_type' => $this->input->post('merk'),
							'tahun' => $this->input->post('tahun'),
							'harga_satuan' => $this->input->post('harga'),
							'sumber_dana' => $this->input->post('dana'),
							'kode_lokasi' => $this->input->post('ruang'),
							'koordinat' => $koordinat
						);
			} else {
				$data = array(
							'kode_barang' => $this->input->post('kode'),
							'nama_barang' => $this->input->post('nama'),
							'merk_type' => $this->input->post('merk'),
							'tahun' => $this->input->post('tahun'),
							'harga_satuan' => $this->input->post('harga'),
							'sumber_dana' => $this->input->post('dana'),
							'kode_lokasi' => $this->input->post('ruang')
						);
			}*/
			$id = $d['id'];
			if($d['koordinat'] != NULL && $d['foto'] != NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'koordinat' => $d['koordinat'],
					'foto' => $d['foto']
				);
			} elseif($d['koordinat'] == NULL AND $d['foto'] != NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'foto' => $d['foto']
				);
			} elseif($d['koordinat'] != NULL AND $d['foto'] == NULL){
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi'],
					'koordinat' => $d['koordinat']
				);
			} else{
				$data = array(
					'kode_barang' => $d['kode_barang'],
					'nama_barang' => $d['nama_barang'],
					'merk_type' => $d['merk_type'],
					'tahun' => $d['tahun'],
					'harga_satuan' => $d['harga_satuan'],
					'sumber_dana' => $d['sumber_dana'],
					'kode_lokasi' => $d['kode_lokasi']
				);
			}
			
			$this->db->where('id_barang',$id);
			$this->db->update('tbl_aset',$data);
		}
		
		//fungsi untuk menghapus data pegawai berdasarkan id pegawai
		function deleteAset($id){
			$this->db->where('id_barang',$id);
			$this->db->delete('tbl_aset');
		}
		
		function getLokasiAset(){
			$sql="select nama_ruang, kode_lokasi from tbl_lokasi order by nama_ruang asc";
			return $this->db->query($sql);
		}
		
		function get_lokasi_by_nama($nama){
			$sql="select * from tbl_lokasi where nama_ruang='".$nama."' order by nama_ruang asc";
			return $this->db->query($sql);
		}
		
		function get_lokasi_by_kode($kode){
			$sql="select * from tbl_lokasi where kode_lokasi='".$kode."'";
			return $this->db->query($sql);
		}
		
		function get_wing4($katakunci, $berdasarkan){
			$sql = "select l.wing from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '4' AND a.kode_lokasi = l.kode_lokasi group by l.wing";
			return $this->db->query($sql);
		}
		
		function get_wing($katakunci, $berdasarkan){
			$sql = "select l.wing from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '5' AND a.kode_lokasi = l.kode_lokasi group by l.wing";
			return $this->db->query($sql);
		}
		
		function get_wing6($katakunci, $berdasarkan){
			$sql = "select l.wing from tbl_aset as a, tbl_lokasi as l where ".$berdasarkan." LIKE '%".$katakunci."%' AND l.levels = '6' AND a.kode_lokasi = l.kode_lokasi group by l.wing";
			return $this->db->query($sql);
		}
		
		/**
		* Count the number of rows
		* @param int $search_string
		* @param int $order
		* @return int
		*/
		
		function count_aset($search_string=null, $order=null){
			$this->db->select('*');
			$this->db->from('tbl_aset');
			if($search_string){
				$this->db->like('nama_barang', $search_string);
			}
			if($order){
				$this->db->order_by($order, 'Asc');
			}
			else{
				$this->db->order_by('id_barang', 'Asc');
			}
			$query = $this->db->get();
			return $query->num_rows();
		}
	}
?>