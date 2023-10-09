package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Iterator;

import dungeonmania.entities.Entity;
import dungeonmania.entities.buildableEntities.Bow;
import dungeonmania.entities.buildableEntities.Shield;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.entities.collectableEntities.Sword;
import dungeonmania.entities.collectableEntities.CollectableEntity;
import dungeonmania.entities.buildableEntities.BuildableEntity;
import dungeonmania.entities.buildableEntities.MidnightArmour;
import dungeonmania.entities.buildableEntities.Sceptre;


public class Inventory implements Serializable {
    private Map<String, List<Entity>> items = new HashMap<String, List<Entity>>(); 

    public Map<String, List<Entity>> getItems() {
        return items;
    }

    public List<ItemResponse> getItemResponses() {
        List<ItemResponse> itemResponses = new ArrayList<ItemResponse>();

        for (String s: items.keySet()) {
            for (Entity o: items.get(s)) {
                ItemResponse r = new ItemResponse(o.getId(), o.getType());
                itemResponses.add(r);
            }
        }

        return itemResponses;
    }

    public List<String> getBuildables(Game g) {
        List<String> buildables = new ArrayList<String>();

        if (items.containsKey("wood") && items.containsKey("arrow")) {
            if (items.get("wood").size() >= 1 && items.get("arrow").size() >= 3) {
                if (!buildables.contains("bow")) {
                    buildables.add("bow");
                }
            }
        }

        if (items.containsKey("wood") && items.containsKey("treasure")) {
            if (items.get("wood").size() >= 2 && items.get("treasure").size() >= 1) {
                if (!buildables.contains("shield")) {
                    buildables.add("shield");
                }
            }
        }

        if (items.containsKey("wood") && items.containsKey("key")) {
            if (items.get("wood").size() >= 2 && items.get("key").size() >= 1) {
                if (!buildables.contains("shield")) {
                    buildables.add("shield");
                }
            }
        } 

        if (items.containsKey("wood") && items.containsKey("sun_stone")) {
            if (items.get("wood").size() >= 2 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("shield")) {
                    buildables.add("shield");
                }
            }
        } 

        if (items.containsKey("wood") && items.containsKey("key") && items.containsKey("sun_stone")) {
            if (items.get("wood").size() >= 1 && items.get("key").size() >= 1 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        }
        
        if (items.containsKey("wood") && items.containsKey("treasure") && items.containsKey("sun_stone")) {
            if (items.get("wood").size() >= 1 && items.get("treasure").size() >= 1 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        } 

        if (items.containsKey("wood") && items.containsKey("sun_stone")) {
            if (items.get("wood").size() >= 1 && items.get("sun_stone").size() >= 2) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        }

        if (items.containsKey("arrow") && items.containsKey("key") && items.containsKey("sun_stone")) {
            if (items.get("arrow").size() >= 2 && items.get("key").size() >= 1 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        }

        if (items.containsKey("arrow") && items.containsKey("treasure") && items.containsKey("sun_stone")) {
            if (items.get("arrow").size() >= 2 && items.get("treasure").size() >= 1 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        } 
        
        if (items.containsKey("arrow") && items.containsKey("sun_stone")) {
            if (items.get("arrow").size() >= 2 && items.get("sun_stone").size() >= 2) {
                if (!buildables.contains("sceptre")) {
                    buildables.add("sceptre");
                }
            }
        }
        
        if ((!g.getEntities().containsKey("zombie_toast") || (g.getEntities().containsKey("zombie_toast") && g.getEntities().get("zombie_toast").size() == 0)) && items.containsKey("sword") && items.containsKey("sun_stone")) {
            if (items.get("sword").size() >= 1 && items.get("sun_stone").size() >= 1) {
                if (!buildables.contains("midnight_armour")) {
                    buildables.add("midnight_armour");
                }
            }
        }
        
        return buildables;

    }

    public void addItem(Entity o) {
        if (items.containsKey(o.getType())) {
            items.get(o.getType()).add(o);
        } else {
            List<Entity> l = new ArrayList<Entity>();
            l.add(o);
            items.put(o.getType(), l);
        }
    }

    public void buildBow(Game g) {
        List<String> l = this.getBuildables(g);
        List<Entity> bowList = new ArrayList<Entity>();
        Bow b = new Bow("bow", g.getConfigVariables().getBowDurability());
        bowList.add(b);

        if (l.contains("bow")) {
            if (items.containsKey("bow")) {
                items.get("bow").add(b);
            } else {
                items.put("bow", bowList);
            }
            items.get("wood").remove(0);
            items.get("arrow").remove(0);
            items.get("arrow").remove(0);
            items.get("arrow").remove(0);
        } 
    }

    public void buildShield(Game g) {
        List<Entity> shieldList = new ArrayList<Entity>();

        Shield s = new Shield("shield", g.getConfigVariables().getShieldDurability(), (double) g.getConfigVariables().getShieldDefence());
        shieldList.add(s);

        if (items.containsKey("wood") && (items.containsKey("key") || items.containsKey("treasure") || items.containsKey("sun_stone"))) {
            if (items.containsKey("key") && items.get("key").size() >= 1 && items.get("wood").size() >= 2) {
                if (items.containsKey("shield")) {
                    items.get("shield").add(s);
                } else {
                    items.put("shield", shieldList);
                }

                items.get("wood").remove(0);
                items.get("wood").remove(0);
                items.get("key").remove(0);

            } else if (items.containsKey("treasure") && items.get("treasure").size() >= 1 && items.get("wood").size() >= 2) {
                if (items.containsKey("shield")) {
                    items.get("shield").add(s);
                } else {
                    items.put("shield", shieldList);
                }

                items.get("wood").remove(0);
                items.get("wood").remove(0);
                items.get("treasure").remove(0);
            } else if (items.get("sun_stone") != null && items.get("sun_stone").size() >= 1 && items.get("wood").size() >= 2) {
                if (items.containsKey("shield")) {
                    items.get("shield").add(s);
                } else {
                    items.put("shield", shieldList);
                }

                items.get("wood").remove(0);
                items.get("wood").remove(0);
            }
        }
    }

    public void buildMidnightArmour(Game g) {
        List<String> l = this.getBuildables(g);
        List<Entity> midnightArmourList = new ArrayList<Entity>();

        MidnightArmour ar = new MidnightArmour("midnight_armour", Integer.MAX_VALUE, (double) g.getConfigVariables().getMindnightArmourAttack(), (double) g.getConfigVariables().getMidnightArmourDefence());
        midnightArmourList.add(ar);

        if (l.contains("midnight_armour")) {
            if (items.containsKey("midnight_armour")) {
                items.get("midnight_armour").add(ar);
            } else {
                items.put("midnight_armour", midnightArmourList);
            }
            items.get("sword").remove(0);
            items.get("sun_stone").remove(0);
        } 
    }

    public void buildSceptre(Game g) {
        List<Entity> sceptreList = new ArrayList<Entity>();

        Sceptre s = new Sceptre("sceptre", g.getConfigVariables().getMindControlDuration());
        sceptreList.add(s);
        
        if (items.containsKey("wood") && items.containsKey("key") && items.containsKey("sun_stone") && items.get("wood").size() >= 1 && items.get("key").size() >= 1 && items.get("sun_stone").size() >= 1) {
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("wood").remove(0);
            items.get("key").remove(0);
            items.get("sun_stone").remove(0);
        }
        else if (items.containsKey("wood") && items.containsKey("treasure") && items.containsKey("sun_stone") && items.get("wood").size() >= 1 && items.get("treasure").size() >= 1 && items.get("sun_stone").size() >= 1) {
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("wood").remove(0);
            items.get("treasure").remove(0);
            items.get("sun_stone").remove(0);
        }
        else if (items.containsKey("arrow") && items.containsKey("key") && items.containsKey("sun_stone") && items.get("arrow").size() >= 2 && items.get("key").size() >= 1 && items.get("sun_stone").size() >= 1) {
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("arrow").remove(0);
            items.get("arrow").remove(0);
            items.get("key").remove(0);
            items.get("sun_stone").remove(0);
        }
        else if (items.containsKey("arrow") && items.containsKey("treasure")  && items.containsKey("sun_stone") && items.get("arrow").size() >= 2 && items.get("treasure").size() >= 1  && items.get("sun_stone").size() >= 1) { 
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("arrow").remove(0);
            items.get("arrow").remove(0);
            items.get("treasure").remove(0);
            items.get("sun_stone").remove(0);
        }
        else if (items.containsKey("wood") && items.containsKey("sun_stone") && items.get("wood").size() >= 1 && items.get("sun_stone").size() >= 2) {
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("wood").remove(0);
            items.get("sun_stone").remove(0);
        }
        else if (items.containsKey("arrow") && items.containsKey("sun_stone") && items.get("arrow").size() >= 2 && items.get("sun_stone").size() >= 2) {
            if (items.containsKey("sceptre")) {
                items.get("sceptre").add(s);
            } else {
                items.put("sceptre", sceptreList);
            }
            items.get("arrow").remove(0);
            items.get("arrow").remove(0);
            items.get("sun_stone").remove(0);
        }
        
    }

    public void removeItem(Entity e) {
        for (String s : items.keySet()) {
            if (items.get(s).contains(e)) {
                items.get(s).remove(e);
            }
        }
    }

    // Get the available types of potions in the inventory
    public List<String> getPotions() {
        List<String> potions = new ArrayList<String>();

       
        if (items.containsKey("invincibility_potion")) {
            if (items.get("invincibility_potion").size() >= 1) {
                potions.add("invincibility_potion");
            }
        }
        if (items.containsKey("invisibility_potion")) {
            if (items.get("invisibility_potion").size() >= 1) {
                potions.add("invisibility_potion");
            }
        }

        return potions;
       
    }
    
    Queue<String> queue = new LinkedList<>();
    
    public Queue<String> getQueue() {
        return this.queue;
    }
    
    public void useInvincibilityPotion(Game g) {
        List<String> l = this.getPotions();
        
        if (l.contains("invincibility_potion")) {
            if (items.get("invincibility_potion").size() >= 1) {
                // If status is already taken -> queue potion
                if (g.getStatus().isInvincible() || g.getStatus().isInvisible()) {
                    this.queue.add("invincibility_potion");
                } else {
                    // Change the player status to Invincibile
                    g.getStatus().becomeInvincible();
                }
               
                // Item is then removed.
                items.get("invincibility_potion").remove(0);
            }
        } 
    }
    
    public void useInvisibilityPotion(Game g) {
        List<String> l = this.getPotions();
        
        if (l.contains("invisibility_potion")) {
            if (items.get("invisibility_potion").size() >= 1) {
                // If status is already taken -> queue potion
                if (g.getStatus().isInvincible() || g.getStatus().isInvisible()) {
                    this.queue.add("invisibility_potion");
                } else {
                    // Change the player status to Invisible
                    g.getStatus().becomeInvisible();
                }
               
                // Item is then removed.
                items.get("invisibility_potion").remove(0);
            }
        } 
    }

    // Remove item from inventory
    public void removeEntity(Entity entity) {
        if (items.containsKey(entity.getType())) {
            items.get(entity.getType()).remove(entity);
        }
    }

    // Check if item is in inventory (status: Inventory)
    public boolean inInventory(Entity entity) {
        if (items.containsKey(entity.getType())) {
            if(items.get(entity.getType()).contains(entity)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWeapon() {
        boolean hasWeapon = false;

        if (items.containsKey("sword")) {
            if (items.get("sword").size() >= 1) {
                hasWeapon = true;
            }
        }

        if (items.containsKey("bow")) {
            if (items.get("bow").size() >= 1) {
                hasWeapon = true;
            }
        } 

        return hasWeapon;
    }

    public Entity findItem(String itemId) {
        for (String s : items.keySet()) {
            for (Entity o : items.get(s)) {
                if (Objects.equals(o.getId(), itemId)) {
                    return o;
                }
            }
        }

        return null;
    }

    public boolean hasKey() {
        if (items.containsKey("key") && items.get("key").size() != 0)
        return true;
        else return false;
    }

    public Entity getKey() {
        return items.get("key").get(0);
    }

    public boolean hasSunStone() {
        if (items.containsKey("sun_stone") && items.get("sun_stone").size() != 0)
        return true;
        else return false;
    }

    // Removes weapons with a durability of 0 from inventory
    public void updateInventory(Game g) {
        if (items.containsKey("sword")) {
            Iterator<Entity> i = items.get("sword").iterator();
            while (i.hasNext()) {
                Entity e = i.next();
                Sword s = (Sword)(CollectableEntity) e;
                if (s.getDurability() <= 0) {
                    i.remove();
                }
            }
        }
        if (items.containsKey("shield")) {
            Iterator<Entity> i = items.get("shield").iterator();
            while (i.hasNext()) {
                Entity e = i.next();
                Shield s = (Shield)(BuildableEntity) e;
                if (s.getDurability() <= 0) {
                    i.remove();
                }
            }
        }
        if (items.containsKey("bow")) {
            Iterator<Entity> i = items.get("bow").iterator();
            while (i.hasNext()) {
                Entity e = i.next();
                Bow b = (Bow)(BuildableEntity) e;
                if (b.getDurability() <= 0) {
                    i.remove();
                }
            }
        }
        if (items.containsKey("sceptre")) {
            Iterator<Entity> i = items.get("sceptre").iterator();
            while (i.hasNext()) {
                Entity e = i.next();
                Sceptre s = (Sceptre)(BuildableEntity) e;
                if (s.getDurability() <= 0) {
                    i.remove();
                }
            }
        }
        
    }


}
