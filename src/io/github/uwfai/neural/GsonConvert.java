package io.github.uwfai.neural;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by carso on 4/17/2017.
 */
public class GsonConvert
{
   public static Matrix convert(LinkedTreeMap ltm) {
      Matrix converted = new Matrix();
      Object inner = ltm.get("matrix");
      for (int i = 0; i < ((ArrayList)inner).size(); ++i)
      {
         Object current = ((ArrayList)inner).get(i);
         if (current instanceof LinkedTreeMap) {
            converted.append(GsonConvert.convert((LinkedTreeMap)current));
         } else if (current instanceof Double) {
            converted.append((double)current);
         }
      }
      return converted;
   }
}
