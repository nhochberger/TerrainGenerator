package model.export;

import model.SurfaceMap;

public interface HeightMapExporter {
    public void export(SurfaceMap heightMap, String filePath);
}
