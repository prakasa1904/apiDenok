			<?php
           
            $attributes = array('class' => 'form-inline reset-margin', 'id' => 'myform');
           
            //save the columns names in a array that we will use as filter         
            $options_petugas = array();    
            foreach ($petugas as $array) {
              foreach ($array as $key => $value) {
                $options_petugas[$key] = $key;
              }
              break;
            }

            echo form_open('admin/petugas', $attributes);
     
             

              echo form_label('kategori:', 'order');
              echo form_dropdown('order', $options_petugas, $order, 'class="span2"');

			  echo form_label('Search:', 'search_string');
              echo form_input('search_string', $search_string_selected);
              $data_submit = array('name' => 'mysubmit', 'class' => 'btn btn-primary', 'value' => 'cari');

          

              echo form_submit($data_submit);

            echo form_close();
            ?>