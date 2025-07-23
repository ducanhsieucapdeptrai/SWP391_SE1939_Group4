CREATE DATABASE quan_ly_vat_tu;
USE quan_ly_vat_tu;

CREATE TABLE Roles (
    RoleId INT PRIMARY KEY,
    RoleName VARCHAR(100) NOT NULL
);
INSERT INTO Roles (RoleId, RoleName)
VALUES
(1, 'Warehouse Manager'),
(2, 'Warehouse Staff'),
(3, 'Director'),
(4, 'Company Staff');




CREATE TABLE Modules (
    ModuleId INT AUTO_INCREMENT PRIMARY KEY,
    ModuleName VARCHAR(100) NOT NULL,
    Description VARCHAR(255) NULL
);
INSERT INTO Modules (ModuleName)
VALUES
  ('User Management'),
  ('Material Management'),
  ('Import/Export Materials'),
  ('Material Requests'),
  ('Statistics & Reports');
  
  
  

CREATE TABLE Functions (
    FunctionId INT AUTO_INCREMENT PRIMARY KEY,
    FunctionName VARCHAR(100) NOT NULL,
    Url VARCHAR(255) NULL, -- Made NULLABLE to allow initial inserts without URL
    ModuleId INT NULL,    -- Made NULLABLE to allow initial inserts without ModuleId
    FOREIGN KEY (ModuleId) REFERENCES Modules(ModuleId)
);

-- Module 1: User Management
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('User List', '/userlist', 1),
('Reset Password Requests', '/reset-pass-list', 1),
('Authorization Matrix', '/user-matrix', 1),
('Request password reset', '/request-new-password', 1),
('Assign user roles', NULL, 1),
('View Notification', '/all-notifications', 1),
('Add User', '/add-user', 1);




-- Module 2: Material Management
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Material Inventory', '/materiallist', 2),
('Add new material', '/material-add' , 2),
('Edit material information', '/materialdetail', 2),
('Add Category/Subcategory', '/add-category' , 2),
('Delete material', NULL, 2),
('Edit Material Information', '/editmaterial', 2),
('Inventory Check', '/inventory-check', 2);


-- Module 3: Import/Export
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Create Request', '/createrequest', 3),
('Warehouse Report', '/warehousereport', 3);

-- Module 4: Requests
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Request List', '/reqlist', 4),
('Purchase Request List', '/purchase-request-list', 4),
('Repair Order List', '/repair-request-list', 4),
('My Requests', '/my-request', 4),
('Task List', '/tasklist', 4),
('Completed Tasks', '/completedTasks', 4),
('View Request Detail', '/request-detail', 4),
('View Purchase Order Detail', '/purchase-order-detail', 4),
('View Repair Order Detail', '/repair-order-detail', 4),
('Approve Request Form', '/approve-request', 4),
('Reject Request', '/approveandrejectrequest', 4),
('Approve Request', '/approveandrejectrequest', 4),
('Create PO button', '/create-purchase-order', 4),
('Create PO', '/create-po', 4),
('View Project', '/project', 4)





;
-- Module 5: Reports
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Advanced Dashboard', '/advanced-dashboard', 5),
('Dashboard', '/dashboard', 5),
('View Statistic', '/material-statistics', 5);






CREATE TABLE RoleFunction (
    RoleId INT NOT NULL,
    FunctionId INT NOT NULL,
    IsActive TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (RoleId, FunctionId),
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId),
    FOREIGN KEY (FunctionId) REFERENCES Functions(FunctionId)
);


-- Gán tất cả quyền cho Warehouse Manager
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 1, FunctionId FROM Functions;

-- Gán các quyền chính cho Warehouse Staff
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 2, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Create Request', 'Warehouse Report',
    'My Requests', 'Task List', 'Dashboard'
);

-- Gán quyền cho Director
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 3, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Request List', 'Purchase Request List',
    'Repair Order List', 'My Requests', 'Completed Tasks',
    'Advanced Dashboard', 'Dashboard','User List','Approve Request','View Repair Order Detail','View Request Detail','View Purchase Order Detail','View Request Detail'
    'Create PO','Approve Request Form','View Project'
);

-- Gán quyền cho Company Staff
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 4, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Create Request', 'My Requests', 'Dashboard'
);





CREATE TABLE Users (
    UserId INT PRIMARY KEY AUTO_INCREMENT,
    FullName VARCHAR(100),
    UserImage VARCHAR(100),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20),
    Password VARCHAR(100),
    RoleId INT,
    IsActive BOOLEAN,
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId)
);

ALTER TABLE Users
  ADD COLUMN DateOfBirth DATE NULL,
  ADD COLUMN Gender VARCHAR(10) NULL,
  ADD COLUMN Address VARCHAR(255) NULL;

INSERT INTO Users (FullName, UserImage, Email, Phone, Password, RoleId, IsActive)
VALUES
('Trần Quản Lý', 'warehousemanager.png','quanlyvattu4@gmail.com', '0900000000', 'quanly123', 1, TRUE),
('Nguyễn Giám Đốc',  'director.png','giamdoc@example.com', '0911111111', 'giamdoc123', 3, TRUE),
('Nguyễn Văn A', 'warehousestaff.png', 'a1@example.com', '0900000001', 'pass1', 2, TRUE),
('Nguyễn Văn B',  'warehousestaff.png','a2@example.com', '0900000002', 'pass2', 2, TRUE),
('Nguyễn Văn C', 'warehousestaff.png', 'a3@example.com', '0900000003', 'pass3', 2, TRUE),
('Trần Thị D',  'warehousestaff.png','a4@example.com', '0900000004', 'pass4', 2, TRUE),
('Lê Văn E',  'warehousestaff.png','a5@example.com', '0900000005', 'pass5', 2, TRUE),
('Hoàng Văn F', 'warehousestaff.png', 'a6@example.com', '0900000006', 'pass6', 2, TRUE),
('Lý Thị G', 'warehousestaff.png', 'a7@example.com', '0900000007', 'pass7', 2, TRUE),
('Đào Văn H', 'warehousestaff.png', 'a8@example.com', '0900000008', 'pass8', 2, TRUE),
('Nguyễn Nhân Viên', 'companystaff.png', 'nhanvienkho8686@gmail.com', '0912345678', 'nhanvien123', 2, TRUE),
('Ngô Thị I', 'companystaff.png', 'b1@example.com', '0900000011', 'pass11', 4, TRUE),
('Phạm Văn J', 'companystaff.png', 'b2@example.com', '0900000012', 'pass12', 4, TRUE),
('Đặng Thị K', 'companystaff.png', 'b3@example.com', '0900000013', 'pass13', 4, TRUE),
('Vũ Văn L', 'companystaff.png', 'b4@example.com', '0900000014', 'pass14', 4, TRUE),
('Cao Thị M', 'companystaff.png', 'b5@example.com', '0900000015', 'pass15', 4, TRUE),
('Bùi Văn N', 'companystaff.png', 'b6@example.com', '0900000016', 'pass16', 4, TRUE),
('Đỗ Thị O', 'companystaff.png', 'b7@example.com', '0900000017', 'pass17', 4, TRUE),
('Tống Văn P', 'companystaff.png', 'b8@example.com', '0900000018', 'pass18', 4, TRUE),
('Mai Thị Q', 'companystaff.png', 'b9@example.com', '0900000019', 'pass19', 4, TRUE),
('Lương Văn R', 'companystaff.png', 'b10@example.com', '0900000020', 'pass20', 4, TRUE);

-- Update user details
UPDATE Users SET DateOfBirth = '1978-04-12', Gender = 'Nam', Address = 'Hà Nội' WHERE UserId = 1;
UPDATE Users SET DateOfBirth = '1975-09-30', Gender = 'Nam', Address = 'Hồ Chí Minh' WHERE UserId = 2;
UPDATE Users SET DateOfBirth = '1990-01-15', Gender = 'Nam', Address = 'Đà Nẵng' WHERE UserId = 3;
UPDATE Users SET DateOfBirth = '1988-03-22', Gender = 'Nam', Address = 'Hải Phòng' WHERE UserId = 4;
UPDATE Users SET DateOfBirth = '1992-07-05', Gender = 'Nam', Address = 'Cần Thơ' WHERE UserId = 5;
UPDATE Users SET DateOfBirth = '1989-11-11', Gender = 'Nữ', Address = 'Bình Dương' WHERE UserId = 6;
UPDATE Users SET DateOfBirth = '1991-02-28', Gender = 'Nam', Address = 'Đồng Nai' WHERE UserId = 7;
UPDATE Users SET DateOfBirth = '1987-06-17', Gender = 'Nam', Address = 'Khánh Hòa' WHERE UserId = 8;
UPDATE Users SET DateOfBirth = '1993-12-01', Gender = 'Nữ', Address = 'Hải Dương' WHERE UserId = 9;
UPDATE Users SET DateOfBirth = '1994-05-20', Gender = 'Nam', Address = 'Bắc Ninh' WHERE UserId = 10;
UPDATE Users SET DateOfBirth = '1990-10-10', Gender = 'Nam', Address = 'Quảng Ninh' WHERE UserId = 11;
UPDATE Users SET DateOfBirth = '1992-08-08', Gender = 'Nữ', Address = 'Thanh Hóa' WHERE UserId = 12;
UPDATE Users SET DateOfBirth = '1986-04-04', Gender = 'Nam', Address = 'Nghệ An' WHERE UserId = 13;
UPDATE Users SET DateOfBirth = '1991-09-09', Gender = 'Nữ', Address = 'Bình Định' WHERE UserId = 14;
UPDATE Users SET DateOfBirth = '1985-12-12', Gender = 'Nam', Address = 'Thừa Thiên Huế' WHERE UserId = 15;
UPDATE Users SET DateOfBirth = '1993-03-03', Gender = 'Nữ', Address = 'Quảng Nam' WHERE UserId = 16;
UPDATE Users SET DateOfBirth = '1989-07-07', Gender = 'Nam', Address = 'Long An' WHERE UserId = 17;
UPDATE Users SET DateOfBirth = '1994-02-02', Gender = 'Nữ', Address = 'Hà Tĩnh' WHERE UserId = 18;
UPDATE Users SET DateOfBirth = '1987-05-05', Gender = 'Nam', Address = 'Kiên Giang' WHERE UserId = 19;
UPDATE Users SET DateOfBirth = '1990-11-11', Gender = 'Nữ', Address = 'Bình Phước' WHERE UserId = 20;
UPDATE Users SET DateOfBirth = '1988-06-06', Gender = 'Nam', Address = 'Đắc Lắk' WHERE UserId = 21;


CREATE TABLE Categories (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName VARCHAR(255) NOT NULL
);
INSERT INTO Categories (CategoryName)
VALUES
('Structural Materials'),
('Finishing Materials'),
('Insulation & Waterproofing'),
('Mechanical & Electrical'),
('Interior Decoration'),
('Other Construction Materials'),
('Hand Tools'),
('Construction Machinery'),
('Safety Equipment');


CREATE TABLE SubCategories (
    SubCategoryId INT AUTO_INCREMENT PRIMARY KEY,
    SubCategoryName VARCHAR(255) NOT NULL,
    CategoryId INT,
    FOREIGN KEY (CategoryId) REFERENCES Categories(CategoryId)
);

INSERT INTO SubCategories (SubCategoryName, CategoryId)
VALUES
('Concrete', 1),
('Structural Steel', 1),
('Bricks', 1),
('Building Stone', 1),
('Structural Timber', 1),
('Flooring Materials', 2),
('Wall Coverings', 2),
('Paints and Coatings', 2),
('Ceiling Materials', 2),
('Thermal Insulation', 3),
('Waterproofing Materials', 3),
('Sound Insulation', 3),
('Electrical Systems', 4),
('Plumbing Systems', 4),
('HVAC Systems', 4),
('Wooden Furniture', 5),
('Decorative Materials', 5),
('Sanitary Equipment', 5),
('Foundation Materials', 6),
('Roofing Materials', 6),
('Door and Window Materials', 6),
('Mixing & Plastering Tools', 7),
('Measuring & Alignment Tools', 7),
('Cutting & Assembly Tools', 7),
('Auxiliary Tools', 7),
('Concrete Machinery', 8),
('Earthmoving Equipment', 8),
('Lifting Equipment', 8),
('Steel Processing Equipment', 8),
('Finishing Machinery', 8),
('Utility Machines', 8),
('Head Protection', 9),
('Eye & Face Protection', 9),
('Hand Protection', 9),
('Footwear', 9),
('Body Protection', 9),
('Fall Protection', 9),
('Respiratory Protection', 9);

CREATE TABLE MaterialStatus (
    StatusId INT AUTO_INCREMENT PRIMARY KEY,
    StatusName VARCHAR(100)
);

INSERT INTO MaterialStatus (StatusId, StatusName) -- Explicitly set StatusId for alignment
VALUES (1, 'New'), (2, 'Used'), (3, 'Damaged');

CREATE TABLE Materials (
    MaterialId INT AUTO_INCREMENT PRIMARY KEY,
    MaterialName VARCHAR(300),
    SubCategoryId INT,
    StatusId INT,
    Image VARCHAR(255),
    Description TEXT,
    Quantity INT DEFAULT 0,
    MinQuantity INT DEFAULT 0,
    Unit VARCHAR(50) DEFAULT 'unit',
    Price DOUBLE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt DATETIME,
    FOREIGN KEY (SubCategoryId) REFERENCES SubCategories(SubCategoryId),
    FOREIGN KEY (StatusId) REFERENCES MaterialStatus(StatusId)
);

INSERT INTO Materials (MaterialName, SubCategoryId, StatusId, Image, Description, Quantity, MinQuantity, Price)
VALUES
('Reinforced Concrete', 1, 1, 'reinforced-concrete.png', 'High strength concrete with steel reinforcement', 100, 20, 1200000),
('Lightweight Concrete', 1, 1, 'lightweight-concrete.png', 'Concrete with low density for non-load-bearing walls', 50, 10, 950000),
('I-Beam 200x100', 2, 1, 'i-beam-200x100.png', 'Standard structural steel I-beam', 200, 30, 1800000),
('H-Beam 300x300', 2, 1, 'h-beam-300x300.png', 'Heavy-duty H-beam', 150, 20, 3500000),
('Red Clay Brick', 3, 1, 'red-clay-brick.png', 'Traditional fired clay brick', 10000, 1000, 1200),
('Non-fired Brick', 3, 1, 'non-fired-brick.png', 'Eco-friendly construction brick', 8000, 800, 1500),
('Granite Stone', 4, 1, 'granite-stone.png', 'Used for flooring and wall cladding', 300, 30, 450000),
('Limestone Block', 4, 1, 'limestone-block.png', 'For foundation and support walls', 250, 25, 400000),
('Pine Timber', 5, 1, 'pine-timber.png', 'Used in structural framing', 500, 50, 700000),
('Oak Beam', 5, 1, 'oak-beam.png', 'Heavy-duty wood for decorative structure', 200, 20, 1100000),
('Ceramic Tile 60x60', 6, 1, 'ceramic-tile-60x60.png', 'Matte finish ceramic tile', 1000, 200, 80000),
('Laminate Flooring', 6, 1, 'laminate-flooring.png', 'Wood-pattern laminate', 600, 100, 120000),
('Wall Tile 30x60', 7, 1, 'wall-tile-30x60.png', 'Glossy finish wall tile', 800, 100, 65000),
('Wallpaper Roll', 7, 1, 'wallpaper-roll.png', 'Modern style wallpaper', 300, 50, 95000),
('Water-based Paint (White)', 8, 1, 'water-based-paint-white.png', 'High coverage, interior paint', 500, 50, 60000),
('Exterior Paint (Blue)', 8, 1, 'exterior-paint-blue.png', 'Weather-resistant paint', 300, 30, 75000),
('Gypsum Ceiling Board', 9, 1, 'gypsum-ceiling-board.png', 'Used for suspended ceilings', 400, 40, 120000),
('Aluminum Ceiling Panel', 9, 1, 'aluminum-ceiling-panel.png', 'Waterproof, reflective surface', 200, 20, 250000),
('Glass Wool Roll', 10, 1, 'glass-wool-roll.png', 'For thermal and acoustic insulation', 100, 10, 130000),
('EPS Foam Sheet', 10, 1, 'eps-foam-sheet.png', 'Lightweight insulation board', 300, 30, 95000),
('Waterproof Membrane', 11, 1, 'waterproof-membrane.png', 'Used for basement and roof waterproofing', 150, 15, 185000),
('Bituminous Coating', 11, 1, 'bituminous-coating.png', 'Used in underground waterproofing', 100, 10, 160000),
('Acoustic Foam Panel', 12, 1, 'acoustic-foam-panel.png', 'Used in studios and meeting rooms', 250, 25, 95000),
('Insulated Wallboard', 12, 1, 'insulated-wallboard.png', 'Double-layer board with insulation', 150, 15, 125000),
('Copper Cable 2x2.5mm', 13, 1, 'copper-cable-2x2.5mm.png', 'Used for home wiring', 1000, 100, 12000),
('PVC Conduit Pipe 20mm', 13, 1, 'pvc-conduit-pipe-20mm.png', 'Protects electrical wires', 800, 80, 18000),
('PPR Pipe 25mm', 14, 1, 'ppr-pipe-25mm.png', 'For hot water supply', 600, 60, 35000),
('PVC Elbow 90°', 14, 1, 'pvc-elbow-90.png', 'Connector for plumbing lines', 1000, 100, 5000),
('Flexible Air Duct', 15, 1, 'flexible-air-duct.png', 'Air distribution ducting', 300, 30, 78000),
('Ceiling Exhaust Fan', 15, 1, 'ceiling-exhaust-fan.png', 'Ventilation device for small rooms', 150, 15, 220000),
('Wooden Cabinet', 16, 1, 'wooden-cabinet.png', 'Wall-hung kitchen cabinet', 100, 10, 1800000),
('Office Desk', 16, 1, 'office-desk.png', 'Standard 1.2m desk', 150, 15, 1250000),
('3D Wall Panel', 17, 1, '3d-wall-panel.png', 'PVC decorative wall panel', 200, 20, 145000),
('Crown Moulding (3m)', 17, 1, 'crown-moulding-3m.png', 'Polystyrene decorative trim', 300, 30, 25000),
('Toilet Bowl Set', 18, 1, 'toilet-bowl-set.png', 'Flush toilet + seat + tank', 80, 8, 1450000),
('Wash Basin Ceramic', 18, 1, 'wash-basin-ceramic.png', 'White ceramic basin', 120, 12, 550000),
('Concrete Pile 300x300', 19, 1, 'concrete-pile-300x300.png', 'Used for deep foundations', 200, 20, 950000),
('River Sand', 19, 1, 'river-sand.png', 'Fine sand for concrete mix', 1000, 100, 450000),
('Clay Roof Tile', 20, 1, 'clay-roof-tile.png', 'Traditional curved tile', 1500, 150, 15000),
('Metal Roofing Sheet', 20, 1, 'metal-roofing-sheet.png', 'Zinc-aluminum coated', 500, 50, 180000),
('Aluminum Sliding Door', 21, 1, 'aluminum-sliding-door.png', '2-panel glass door', 100, 10, 2450000),
('uPVC Window 1x1m', 21, 1, 'upvc-window-1x1m.png', 'White-framed tilt window', 120, 12, 1350000),
('trowel', 22, 1, 'trowel.png', 'Hand tool: used for mixing mortar, scooping materials, plastering.', 200, 20, 300000),
('measuring_tape', 23, 1, 'measuring_tape.png', 'Tools: tape measure, meter rule used for measuring lengths and checking alignment.', 150, 15, 200000),
('hammer', 24, 1, 'hammer.png', 'Hand tool: hammer used for knocking, assembling, repair.', 180, 20, 250000),
('pliers', 24, 1, 'pliers.png', 'Hand tool: pliers for gripping, cutting, repair.', 150, 15, 200000),
('wrench', 24, 1, 'wrench.png', 'Hand tool: wrench for turning nuts, bolts.', 150, 15, 220000),
('screwdriver', 24, 1, 'screwdriver.png', 'Hand tool: screwdriver for screwing/unscrewing.', 200, 20, 180000),
('electric_drill', 24, 1, 'electric_drill.png', 'Power tool: drill for drilling wood, metal, concrete.', 50, 5, 1200000),
('angle_grinder', 24, 1, 'angle_grinder.png', 'Power tool: grinder for grinding and cutting materials.', 40, 5, 1100000),
('cutting_machine', 24, 1, 'cutting_machine.png', 'Power tool: saw/cutter for wood, metal, tiles.', 30, 5, 1500000),
('mortar_mixer', 22, 1, 'mortar_mixer.png', 'Mixer: used to mix mortar, cement. Portable.', 10, 1, 9600000),
('plumb_bob', 23, 1, 'plumb_bob.png', 'Tool: plumb line used for establishing vertical alignment.', 100, 10, 150000),
('tile_cutter', 24, 1, 'tile_cutter.png', 'Tool: tile cutter used for cutting tiles and stones.', 20, 2, 1800000),
('notched_trowel', 22, 1, 'notched_trowel.png', 'Tool: notched trowel for tile and stone application.', 50, 5, 300000),
('corner_trowel', 22, 1, 'corner_trowel.png', 'Tool: corner trowel for finishing corners.', 30, 3, 350000),
('formwork', 7, 1, 'formwork.png', 'Tool: molds and edge forms for shaping structural elements.', 20, 2, 2500000),
('strap', 7, 1, 'strap.png', 'Tool: metal strap for alignment and formwork.', 100, 10, 200000),
('garden_spade', 22, 1, 'garden_spade.png', 'Shovel/spade used for mixing mortar and scooping materials.', 80, 8, 300000),
('concrete_mixer', 26, 1, 'concrete_mixer.png', 'Concrete mixer machine: used to mix fresh concrete on site.', 5, 1, 9600000),
('excavator', 27, 1, 'excavator.png', 'Earthmoving machinery: used for digging and leveling.', 3, 1, 750000000),
('bulldozer', 27, 1, 'bulldozer.png', 'Earthmoving machinery: used for ground leveling.', 2, 1, 800000000),
('roller_compactor', 27, 1, 'roller_compactor.png', 'Machinery: compacts soil and stones.', 2, 1, 700000000),
('crane', 28, 1, 'crane.png', 'Lifting equipment: used for lifting and lowering construction materials.', 1, 1, 1500000000),
('iron_cutter', 29, 1, 'iron_cutter.png', 'Steel processing: iron cutting machine.', 3, 1, 500000000),
('concrete_pump', 29, 1, 'concrete_pump.png', 'Concrete pumping machine for site placement.', 2, 1, 600000000),
('vibrator', 27, 1, 'vibrator.png', 'Vibrating machine: for leveling and compacting concrete surfaces.', 5, 1, 300000000),
('power_generator', 15, 1, 'power_generator.png', 'Generator: provides power for tools and machinery.', 4, 1, 250000000),
('tile_saw', 29, 1, 'tile_saw.png', 'Tile saw machine for cutting ceramic tiles/stones.', 3, 1, 400000000),
('hard_hat', 32, 1, 'hard_hat.png', 'Safety helmet to protect head from falling objects.', 100, 20, 200000),
('safety_glasses', 33, 1, 'safety_glasses.png', 'Protective eyewear against dust and debris.', 150, 30, 150000),
('work_gloves', 34, 1, 'work_gloves.png', 'Protective gloves against chemicals and sharp objects.', 200, 40, 80000),
('safety_boots', 35, 1, 'safety_boots.png', 'Protective footwear to prevent injuries.', 100, 20, 600000),
('protective_suit', 36, 1, 'protective_suit.png', 'Body protection suit against dust, chemicals.', 50, 10, 300000),
('safety_harness', 37, 1, 'safety_harness.png', 'Fall protection: harness for working at height.', 80, 10, 250000),
('respirator_mask', 38, 1, 'respirator_mask.png', 'Respiratory protection: mask against harmful environments.', 150, 30, 120000);


UPDATE Materials SET Quantity = 0;
UPDATE Materials SET Unit = 'm³' WHERE MaterialName LIKE '%Concrete%';
UPDATE Materials SET Unit = 'pcs' WHERE MaterialName LIKE '%Brick%' OR MaterialName LIKE '%Tile%';
UPDATE Materials SET Unit = 'kg' WHERE MaterialName LIKE '%Paint%' OR MaterialName LIKE '%Coating%';
UPDATE Materials SET Unit = 'm' WHERE MaterialName LIKE '%Pipe%' OR MaterialName LIKE '%Cable%';
UPDATE Materials SET Unit = 'set' WHERE MaterialName LIKE '%Toilet Bowl%' OR MaterialName LIKE '%Cabinet%';
UPDATE Materials SET Unit = 'roll' WHERE MaterialName LIKE '%Wallpaper Roll%' OR MaterialName LIKE '%Glass Wool%';
UPDATE Materials SET Unit = 'sheet' WHERE MaterialName LIKE '%Board%' OR MaterialName LIKE '%Panel%';
UPDATE Materials SET Unit = 'unit' WHERE Unit = 'unit';

SET SQL_SAFE_UPDATES = 1;

CREATE TABLE MaterialInventory (
    InventoryId INT AUTO_INCREMENT PRIMARY KEY,
    MaterialId INT,
    StatusId INT, -- 1: New, 2: Used, 3: Damaged
    Quantity INT DEFAULT 0,
    LastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId),
    FOREIGN KEY (StatusId) REFERENCES MaterialStatus(StatusId),
    UNIQUE (MaterialId, StatusId) -- Mỗi vật tư + trạng thái chỉ có 1 dòng
);


CREATE TABLE Project (
    ProjectId INT PRIMARY KEY AUTO_INCREMENT,
    ProjectName VARCHAR(255) NOT NULL,
    Description TEXT,
    StartDate DATE,
    EndDate DATE,
    ManagerId INT,
    IsDeleted BOOLEAN DEFAULT 0,
    AttachmentPath VARCHAR(255),
    Status VARCHAR(50) DEFAULT 'Active',
    FOREIGN KEY (ManagerId) REFERENCES Users(UserId)
);


CREATE TABLE RequestType (
    RequestTypeId INT AUTO_INCREMENT PRIMARY KEY,
    RequestTypeName VARCHAR(100)
);

INSERT INTO RequestType (RequestTypeName)
VALUES
('Export'),
('Import'),
('Purchase'),
('Repair');




CREATE TABLE RequestSubType (
    SubTypeId INT AUTO_INCREMENT PRIMARY KEY,
    RequestTypeId INT NOT NULL,
    SubTypeName VARCHAR(100) NOT NULL,
    Description TEXT,
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId)
);

-- Subtypes for Export (RequestTypeId = 1)
INSERT INTO RequestSubType (RequestTypeId, SubTypeName) VALUES
(1, 'For Construction'),
(1, 'For Equipment Repair');

-- Subtypes for Import (RequestTypeId = 2)
INSERT INTO RequestSubType (RequestTypeId, SubTypeName) VALUES
(2, 'New Purchase'),
(2, 'Returned from Repair'),
(2, 'Returned from Usage');





CREATE TABLE RequestStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

INSERT INTO RequestStatus (StatusCode, Description)
VALUES
('Pending', 'Waiting for approval'),
('Approved', 'Approved by director'),
('Rejected', 'Request has been rejected'),
('Completed', 'Successfull');



CREATE TABLE RequestList (
    RequestId INT AUTO_INCREMENT PRIMARY KEY,
    RequestedBy INT,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    RequestTypeId INT,
    SubTypeId INT NULL,
    Note TEXT,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    ApprovalNote TEXT,
    AssignedStaffId INT,
    ArrivalDate DATETIME,
    IsTransferredToday BOOLEAN DEFAULT FALSE,
    IsUpdated BOOLEAN DEFAULT FALSE NOT NULL,
    IsCompleted BOOLEAN DEFAULT FALSE,
    ProjectId INT NULL, -- Made NULLABLE
    FOREIGN KEY (ProjectId) REFERENCES Project(ProjectId),
    FOREIGN KEY (RequestedBy) REFERENCES Users(UserId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (SubTypeId) REFERENCES RequestSubType(SubTypeId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (AssignedStaffId) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES RequestStatus(StatusCode)
);

INSERT INTO RequestList
(RequestedBy, RequestDate, RequestTypeId, SubTypeId, Note, Status, ApprovedBy, ApprovedDate, ApprovalNote, AssignedStaffId, ArrivalDate, IsTransferredToday, IsUpdated, IsCompleted)
VALUES
(12, '2025-05-25 07:30:00', 1, 1, 'Export for foundation of Building A', 'Approved', 2, '2025-05-25 08:00:00', 'Approved on schedule', 5, '2025-05-25 09:00:00', FALSE, FALSE, FALSE),
(13, '2025-05-26 08:30:00', 1, 1, 'Export finishing materials to site B', 'Approved', 2, '2025-05-26 09:00:00', 'Export approved.', NULL, NULL, FALSE, FALSE, FALSE),
(5, NOW(), 1, 1, 'Export leftover cement', 'Rejected', 2, NOW(), 'Not needed. Keep for future use.', NULL, NULL, FALSE, FALSE, FALSE),
(5, '2025-06-15 10:30:00', 1, 1, 'Export granite stone to site D', 'Approved', 2, '2025-06-15 11:00:00', 'Site D confirmed.', 3, '2025-06-15 13:00:00', FALSE, FALSE, FALSE),
(9, '2025-06-17 10:20:00', 1, 1, 'Export timber for site C', 'Approved', 2, '2025-06-17 10:50:00', 'Export confirmed.', NULL, NULL, FALSE, FALSE, FALSE),
(12, '2025-06-19 08:00:00', 1, 2, 'Export wall tiles to branch B', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(4, NOW(), 3, NULL, 'Purchase office air conditioners', 'Pending', NULL, NULL, NULL, 7, NULL, FALSE, FALSE, FALSE),
(14, '2025-05-29 10:00:00', 3, NULL, 'Propose purchase of office chairs', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(7, '2025-06-16 09:30:00', 3, NULL, 'Purchase new safety helmets', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(11, '2025-06-18 11:00:00', 3, NULL, 'Purchase fire extinguishers', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(6, NOW(), 4, NULL, 'Repair broken drill machine', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(8, NOW(), 4, NULL, 'Repair water leakage at storage', 'Pending', NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, FALSE),
(6, '2025-06-16 08:15:00', 4, NULL, 'Repair concrete cutter', 'Pending', NULL, NULL, NULL, 5, NULL, FALSE, FALSE, FALSE),
(10, '2025-06-18 09:00:00', 4, NULL, 'Repair lighting system', 'Pending', NULL, NULL, NULL, 6, NULL, FALSE, FALSE, FALSE);




CREATE TABLE RequestDetail (
    RequestId INT,
    MaterialId INT,
    Quantity INT,
    ActualQuantity INT DEFAULT 0,
    PRIMARY KEY (RequestId, MaterialId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, ActualQuantity) VALUES
(1, 1, 20, 0), -- RequestId = 1 (Export for foundation of Building A)
(1, 3, 10, 0),
(2, 11, 100, 0), -- RequestId = 2 (Export finishing materials to site B)
(2, 13, 50, 0),
(3, 16, 50, 0), -- RequestId = 3 (Export leftover cement - Rejected)
(4, 7, 50, 0), -- RequestId = 4 (Export granite stone to site D)
(5, 9, 80, 0), -- RequestId = 5 (Export timber for site C)
(6, 13, 100, 0), -- RequestId = 6 (Export wall tiles to branch B)
(7, 15, 3, 0), -- RequestId = 7 (Purchase office air conditioners)
(7, 11, 150, 0),
(8, 25, 5, 0), -- RequestId = 8 (Propose purchase of office chairs)
(9, 35, 20, 0), -- RequestId = 9 (Purchase new safety helmets)
(10, 28, 30, 0), -- RequestId = 10 (Purchase fire extinguishers)
(11, 17, 1, 0), -- RequestId = 11 (Repair broken drill machine)
(11, 3, 2, 0),
(12, 19, 1, 0), -- RequestId = 12 (Repair water leakage at storage)
(13, 3, 1, 0), -- RequestId = 13 (Repair concrete cutter)
(14, 26, 1, 0); -- RequestId = 14 (Repair lighting system)



-- ========== NOTIFICATIONS ==========
CREATE TABLE NotificationTypes (
    TypeId INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(255) NOT NULL
);
INSERT INTO NotificationTypes (TypeName) VALUES
('CATEGORY_ADDED'),
('SUBCATEGORY_ADDED'),
('MATERIAL_ADDED'),
('REQUEST_CREATED'),
('REQUEST_APPROVED'),
('REQUEST_REJECTED'),
('ORDER_CREATED'),
('ORDER_APPROVED'),
('ORDER_REJECTED'),
('TASK_COMPLETED'),
('TASK_STARTED'),
('SYSTEM');


CREATE TABLE Notifications (
    NotificationId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT,
    TypeId INT NOT NULL,
    Title VARCHAR(255),
    Message VARCHAR(255) NOT NULL,
    Url VARCHAR(255),
    RelatedId INT,
    RelatedType VARCHAR(50),
    Priority VARCHAR(20) DEFAULT 'MEDIUM',
    IsRead BOOLEAN DEFAULT FALSE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (TypeId) REFERENCES NotificationTypes(TypeId)
);




CREATE TABLE PurchaseOrderStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

INSERT INTO PurchaseOrderStatus (StatusCode, Description) VALUES
('Pending', 'Waiting for director approval'),
('Approved', 'Approved by director'),
('Rejected', 'Rejected by director');



CREATE TABLE PurchaseOrderList (
    POId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT NOT NULL,
    CreatedBy INT NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TotalPrice DOUBLE,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    Note TEXT,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES PurchaseOrderStatus(StatusCode)
);
INSERT INTO PurchaseOrderList (POId, RequestId, CreatedBy, CreatedDate, TotalPrice, Status, Note) VALUES
(1, 7, 4, NOW(), 12180000, 'Pending', 'Purchase office air conditioners'),
(2, 10, 11, NOW(), 6600000, 'Pending', 'Purchase fire extinguishers urgently'),
(3, 8, 14, NOW(), 1260000, 'Pending', 'Propose purchase of office chairs');




CREATE TABLE PurchaseOrderDetail (
    POId INT,
    MaterialId INT,
    Quantity INT,
    UnitPrice DOUBLE,
    Total DOUBLE,
    PRIMARY KEY (POId, MaterialId),
    FOREIGN KEY (POId) REFERENCES PurchaseOrderList(POId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

INSERT INTO PurchaseOrderDetail (POId, MaterialId, Quantity, UnitPrice, Total) VALUES
(1, 15, 3, 60000, 180000),
(1, 11, 150, 80000, 12000000),
(2, 28, 30, 220000, 6600000),
(3, 25, 5, 12000, 60000),
(3, 17, 10, 120000, 1200000);



-- Bảng lưu trạng thái phiếu sửa chữa
CREATE TABLE RepairOrderStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

-- Dữ liệu mẫu cho trạng thái phiếu sửa chữa
INSERT INTO RepairOrderStatus (StatusCode, Description) VALUES
('Pending', 'Waiting for director approval'),
('Approved', 'Approved by director'),
('Rejected', 'Rejected by director');

-- Bảng danh sách phiếu sửa chữa
CREATE TABLE RepairOrderList (
    ROId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT NOT NULL,
    CreatedBy INT NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    Note TEXT,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES RepairOrderStatus(StatusCode)
);

-- Bảng chi tiết phiếu sửa chữa
CREATE TABLE RepairOrderDetail (
    ROId INT,
    MaterialId INT,
    Quantity INT,
    UnitPrice DOUBLE,
    MNote TEXT,
    PRIMARY KEY (ROId, MaterialId),
    FOREIGN KEY (ROId) REFERENCES RepairOrderList(ROId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

CREATE TABLE TaskLog (
    TaskId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT,
    RequestTypeId INT,
    MaterialId INT,
    StaffId INT,
    Quantity INT,
    SlipCode VARCHAR(30) UNIQUE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId),
    FOREIGN KEY (StaffId) REFERENCES Users(UserId)
);

CREATE TABLE TaskSlipDetail (
    SlipId INT AUTO_INCREMENT PRIMARY KEY,
    TaskId INT,
    MaterialId INT,
    Quantity INT,
    FOREIGN KEY (TaskId) REFERENCES TaskLog(TaskId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

CREATE TABLE PasswordResetRequest (
    RequestId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT NOT NULL,
    RequestedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(20) DEFAULT 'Pending',
    ProcessedAt DATETIME,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

CREATE TABLE Events (
    EventId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Description TEXT,
    StartTime DATETIME NOT NULL,
    EndTime DATETIME,
    EventType VARCHAR(50),
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);









